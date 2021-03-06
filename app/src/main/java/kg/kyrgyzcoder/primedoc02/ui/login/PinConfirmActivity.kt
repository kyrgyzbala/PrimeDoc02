package kg.kyrgyzcoder.primedoc02.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc02.MainActivity
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.util.USER_CREDENTIALS
import kg.kyrgyzcoder.primedoc02.util.USER_PHONE
import kg.kyrgyzcoder.primedoc02.util.USER_PIN
import kg.kyrgyzcoder.primedoc02.util.USER_PIN_KEY

import kotlinx.android.synthetic.main.activity_pin_confirm.*

class PinConfirmActivity : AppCompatActivity() {

    private var pinOne: String = ""
    private var pin: String = ""

    private var indexDot: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_confirm)

        val sp = getSharedPreferences(USER_PIN, Context.MODE_PRIVATE)
        pinOne = sp.getString(USER_PIN_KEY, "")!!

        indexDot = 1
        button1PinTwo.setOnClickListener {
            pin += "1"
            setDot()
            indexDot++
        }
        button2PinTwo.setOnClickListener {
            pin += "2"
            setDot()
            indexDot++
        }
        button3PinTwo.setOnClickListener {
            pin += "3"
            setDot()
            indexDot++
        }
        button4PinTwo.setOnClickListener {
            pin += "4"
            setDot()
            indexDot++
        }
        button5PinTwo.setOnClickListener {
            pin += "5"
            setDot()
            indexDot++
        }
        button6PinTwo.setOnClickListener {
            pin += "6"
            setDot()
            indexDot++
        }
        button7PinTwo.setOnClickListener {
            pin += "7"
            setDot()
            indexDot++
        }
        button8PinTwo.setOnClickListener {
            pin += "8"
            setDot()
            indexDot++
        }
        button9PinTwo.setOnClickListener {
            pin += "9"
            setDot()
            indexDot++
        }
        button0PinTwo.setOnClickListener {
            pin += "0"
            setDot()
            indexDot++
        }

        buttondelPinTwo.setOnClickListener {
            if (indexDot > 1) {
                pin = pin.dropLast(1)
                deleteDot()
                indexDot--
                Log.d("PinConfirmActivity", "onCreate (line 86): $indexDot pin: $pin")
            }
        }

    }

    private fun deleteDot() {
        when (indexDot) {
            1 -> {
                dot1PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            2 -> {
                dot2PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            3 -> {
                dot3PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            else -> {
                dot4PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_inactive
                    )
                )
            }
        }
    }

    private fun setDot() {
        when (indexDot) {
            1 -> {
                dot1PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_active
                    )
                )
            }
            2 -> {
                dot2PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_active
                    )
                )
            }
            3 -> {
                dot3PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_active
                    )
                )
            }
            else -> {
                dot4PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pin_active
                    )
                )
            }
        }
        if (pin.length == 4) {
            if (pin == pinOne) {
                savePin(pin)

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(intent)
            } else {
                errorPin()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            signInFirebase()
    }

    private fun signInFirebase() {

        val sp = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val phone = sp.getString(USER_PHONE, "")
        Log.d("PinConfirmActivity", "createNewUser (line 196): ${phone?.takeLast(12)}")
        val email = "${phone?.takeLast(12)}@gmail.com"

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, phone!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("PinConfirmActivity", "signInFirebase (line 196): signInSuccess")
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val db = FirebaseFirestore.getInstance()
                        val map = mutableMapOf<String, String>()
                        map["userType"] = "ADMIN"
                        map["userPhone"] = phone
                        db.collection("users").document(user.uid).set(map)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(
                                        "PinConfirmActivity",
                                        "signInFirebase (line 207): success"
                                    )
                                }
                            }
                    }
                } else {
                    Log.d("PinConfirmActivity", "signInFirebase (line 212): ${it.exception}")
                }
            }

    }

    private fun savePin(pin: String) {
        val sp = getSharedPreferences(USER_PIN, Context.MODE_PRIVATE).edit()
        sp.putString(USER_PIN_KEY, pin)
        sp.apply()
    }

    private fun errorPin() {
        pinDoesNotMatchTextView.visibility = View.VISIBLE
        dot1PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_pin_error
            )
        )
        dot2PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_pin_error
            )
        )
        dot3PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_pin_error
            )
        )
        dot4PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_pin_error
            )
        )
    }

}