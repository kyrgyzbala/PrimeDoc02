package kg.kyrgyzcoder.primedoc02.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kg.kyrgyzcoder.primedoc02.MainActivity
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.util.EXTRA_CALL_REF
import kg.kyrgyzcoder.primedoc02.util.EXTRA_USER_ID
import kg.kyrgyzcoder.primedoc02.util.EXTRA_USER_NAME
import kotlinx.android.synthetic.main.activity_call_user.*

class CallUserActivity : AppCompatActivity() {

    private var clientId: String = ""

    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_user)

        clientId = intent.getStringExtra(EXTRA_USER_ID)!!
        getClientData()
    }

    private fun getClientData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(clientId).get().addOnSuccessListener {
            val phone = it.getString("userPhone")
            userNameTextView.text = phone
        }
    }


    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            makePhoneCall(user)
    }

    private fun makePhoneCall(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val ref =
            db.collection("users").document(clientId).collection("call").document("calling")
        ref.get().addOnSuccessListener {
            if (it.exists() && !it.getString("uid").isNullOrEmpty()) {
                Toast.makeText(this, "Doctor is busy at the moment! Try later", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val callData = mutableMapOf<String, Any>()
                callData["uid"] = user.uid
                callData["accepted"] = false
                callData["declined"] = false
                ref.set(callData).addOnSuccessListener {
                    addSnapListener(ref)
                    setEndCall(ref)
                }
            }
        }

    }

    private fun setEndCall(ref: DocumentReference) {
        cancelCallButton.setOnClickListener {
            val callData = mutableMapOf<String, Any>()
            callData["uid"] = ""
            callData["accepted"] = false
            callData["declined"] = false
            ref.set(callData).addOnSuccessListener {
                Toast.makeText(this, "Call Ended!!!", Toast.LENGTH_LONG).show()
                listener?.remove()
                onBackPressed()
            }
        }
    }

    private fun addSnapListener(ref: DocumentReference) {
        listener = ref.addSnapshotListener { value, error ->

            if (value != null && value.exists()) {
                val accepted = value.getBoolean("accepted")
                val declined = value.getBoolean("declined")

                if (accepted != null && accepted == true) {
                    Toast.makeText(this, "Call Accepted", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CallActualActivity::class.java)
                    intent.putExtra(EXTRA_CALL_REF, ref.path)
                    intent.putExtra(EXTRA_USER_NAME, userNameTextView.text)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                }
                if (declined != null && declined == true) {
                    Toast.makeText(this, "Call Declined", Toast.LENGTH_LONG).show()
                    listener?.remove()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                }

            }

        }
    }

}