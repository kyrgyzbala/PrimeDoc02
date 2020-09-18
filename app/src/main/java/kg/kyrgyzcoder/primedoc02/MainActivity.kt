package kg.kyrgyzcoder.primedoc02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kg.kyrgyzcoder.primedoc02.ui.chat.CallReceiveActivity
import kg.kyrgyzcoder.primedoc02.util.EXTRA_RECEIVE_ID
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        nav_bottom_view.setOnNavigationItemSelectedListener(this)
        val sectionPagerAdapter = MainPagerAdapter(this)
        view_pager.adapter = sectionPagerAdapter
        view_pager.isUserInputEnabled = false

        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> nav_bottom_view.menu.findItem(R.id.nav_time_table).isChecked = true
                    1 -> nav_bottom_view.menu.findItem(R.id.nav_med_card).isChecked = true
                    else -> nav_bottom_view.menu.findItem(R.id.nav_chat).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        view_pager.currentItem = when (item.itemId) {
            R.id.nav_time_table -> 0
            R.id.nav_med_card -> 1
            else -> 2
        }
        return true
    }

    override fun onBackPressed() {
    }


    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            setUserOnline(user)
            addCallListener(user)
        }
    }

    private fun addCallListener(user: FirebaseUser) {
        val ref = FirebaseFirestore.getInstance().collection("doctors").document(user.uid)
            .collection("call").document("calling")
        ref.addSnapshotListener { value, error ->
            if (value != null && value.exists()) {
                val uid = value.getString("uid")
                if (uid != null && uid != "") {
                    val intent = Intent(this, CallReceiveActivity::class.java)
                    intent.putExtra(EXTRA_RECEIVE_ID, uid)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setUserOnline(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val map = mutableMapOf<String, Any>()
        map["isOnline"] = true
        db.collection("doctors").document(user.uid).set(map, SetOptions.merge())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        "Online",
                        "signInFirebase (line 207): success"
                    )
                }
            }
    }

}