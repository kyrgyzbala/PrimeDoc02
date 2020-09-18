package kg.kyrgyzcoder.primedoc02

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import kg.kyrgyzcoder.primedoc02.data.login.ModelLogin
import kg.kyrgyzcoder.primedoc02.ui.login.LoginActivity
import kg.kyrgyzcoder.primedoc02.ui.login.PinConfirmActivity
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc02.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.statusBarColor = Color.parseColor("#ED5168")

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            checkIsRegistered()
        }, 2000)
    }


    private fun checkIsRegistered() {
        val sp = getSharedPreferences(USER_STATUS, Context.MODE_PRIVATE)
        val registered = sp.getString(USER_STATUS_KEY, "")
        if (registered == "") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(intent)
        } else {
            updateUserCred()
        }
    }

    private fun updateUserCred() {
        val sp = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val userName = sp.getString(USER_PHONE, "")
        val pwd = sp.getString(USER_PWD, "")

        val model = ModelLogin(username = userName!!, password = pwd!!)

        GlobalScope.launch(Dispatchers.Main) {
            val response = registerViewModel.loginWithPwd(model)

            if (response.isSuccessful) {
                val resp = response.body()!!

                val edit2 =
                    getSharedPreferences(USER_CRED, Context.MODE_PRIVATE).edit()
                edit2.putInt(USER_ID_KEY, resp.id)
                edit2.putString(USER_ACCESS_TOKEN, resp.accessToken)
                edit2.putString(USER_CHAT_TOKEN, resp.chatToken)
                edit2.putString(USER_CHAT_EXP, resp.tokenExpirationTime)
                edit2.putString(USER_REFRESH_EXP, resp.refreshExpirationTime)
                edit2.putString(USER_REFRESH_TOKEN, resp.refreshToken)
                edit2.apply()

                val intent = Intent(this@SplashActivity, PinConfirmActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(intent)
            }
        }

    }

}