package kg.kyrgyzcoder.primedoc02.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.util.EXTRA_PIN
import kg.kyrgyzcoder.primedoc02.util.USER_CREDENTIALS
import kg.kyrgyzcoder.primedoc02.util.USER_PHONE
import kotlinx.android.synthetic.main.fragment_pin_one.*


class PinOneFragment : Fragment() {

    private var pin: String = ""
    private var indexDot: Int = 1

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        indexDot = 1
        button1PinOne.setOnClickListener {
            pin += "1"
            setDot()
            indexDot++
        }
        button2PinOne.setOnClickListener {
            pin += "2"
            setDot()
            indexDot++
        }
        button3PinOne.setOnClickListener {
            pin += "3"
            setDot()
            indexDot++
        }
        button4PinOne.setOnClickListener {
            pin += "4"
            setDot()
            indexDot++
        }
        button5PinOne.setOnClickListener {
            pin += "5"
            setDot()
            indexDot++
        }
        button6PinOne.setOnClickListener {
            pin += "6"
            setDot()
            indexDot++
        }
        button7PinOne.setOnClickListener {
            pin += "7"
            setDot()
            indexDot++
        }
        button8PinOne.setOnClickListener {
            pin += "8"
            setDot()
            indexDot++
        }
        button9PinOne.setOnClickListener {
            pin += "9"
            setDot()
            indexDot++
        }
        button0PinOne.setOnClickListener {
            pin += "0"
            setDot()
            indexDot++
        }

        buttondelPinOne.setOnClickListener {
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
                dot1PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            2 -> {
                dot2PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            3 -> {
                dot3PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            else -> {
                dot4PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
        }
    }

    private fun setDot() {
        when (indexDot) {
            1 -> {
                dot1PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            2 -> {
                dot2PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            3 -> {
                dot3PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            else -> {
                dot4PinOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
        }
        if (pin.length == 4) {
            val bundle = Bundle()
            bundle.putString(EXTRA_PIN, pin)
            findNavController().navigate(R.id.action_pinOneFragment_to_pinTwoFragment, bundle)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        if (user == null)
            createNewUser()

    }

    private fun createNewUser() {
        val sp = requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val phone = sp.getString(USER_PHONE, "")
        Log.d("PinOneFragment", "createNewUser (line 196): ${phone?.takeLast(12)}")
        val email = "${phone?.takeLast(12)}@gmail.com"
        mAuth.createUserWithEmailAndPassword(email, phone?.takeLast(12)!!).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val db = FirebaseFirestore.getInstance()
                    val map = mutableMapOf<String, String>()
                    map["userType"] = "ADMIN"
                    map["userPhone"] = phone
                    db.collection("users").document(user.uid).set(map)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("PinOneFragment", "createNewUser (line 210): user create success")
                            }
                        }
                }
            } else {
                Toast.makeText(requireContext(), "FAILED", Toast.LENGTH_SHORT).show()
                Log.d("PinOneFragment", "createNewUser (line 201): ${it.exception}")
            }
        }
    }

}