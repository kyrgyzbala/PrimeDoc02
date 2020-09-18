package kg.kyrgyzcoder.primedoc02.ui.chat

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kg.kyrgyzcoder.primedoc02.MainActivity
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.util.EXTRA_CALL_REF
import kg.kyrgyzcoder.primedoc02.util.EXTRA_RECEIVE_ID
import kg.kyrgyzcoder.primedoc02.util.EXTRA_USER_NAME
import kotlinx.android.synthetic.main.activity_call_receive.*

class CallReceiveActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_receive)

        val uid = intent.getStringExtra(EXTRA_RECEIVE_ID)!!

        getUserPhone(uid)
    }

    private fun getUserPhone(uid: String) {
        val ref = FirebaseFirestore.getInstance().collection("users").document(uid)
        ref.get().addOnSuccessListener {
            val phone = it.getString("userPhone")
            userNameTextViewReceive.text = phone
            setClicks()
        }
    }

    private fun setClicks() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val ref = FirebaseFirestore.getInstance().collection("doctors").document(user.uid)
            .collection("call").document("calling")


        acceptCallButtonReceive.setOnClickListener {
            val map = mutableMapOf<String, Boolean>()
            map["accepted"] = true
            ref.set(map, SetOptions.merge()).addOnSuccessListener {
                val intent = Intent(this, CallActualActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(EXTRA_CALL_REF, ref.path)
                intent.putExtra(EXTRA_USER_NAME, userNameTextViewReceive.text.toString())
                finish()
                startActivity(intent)
            }
        }
        cancelCallButtonReceive.setOnClickListener {
            val map = mutableMapOf<String, Any>()
            map["accepted"] = false
            map["declined"] = true
            map["uid"] = ""
            ref.set(map, SetOptions.merge()).addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                finish()
                startActivity(intent)
            }
        }
    }
}