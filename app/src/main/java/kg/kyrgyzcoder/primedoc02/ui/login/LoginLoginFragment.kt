package kg.kyrgyzcoder.primedoc02.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.data.login.ModelLogin
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc02.util.*

import kotlinx.android.synthetic.main.fragment_login_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class LoginLoginFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)
        val bundle = arguments
        val phoneNumber = bundle?.getString(EXTRA_USER_PHONE)

        nextButtonPwdConfirm.setOnClickListener {
            if (pwdConfirmationEditText.text.isNullOrEmpty()) {
                pwdConfirmationEditText.error = getString(R.string.requiredField)
            } else {
                loginWithPwd(phoneNumber)
            }
        }

    }

    private fun loginWithPwd(phoneNumber: String?) {
        val model = ModelLogin(
            password = pwdConfirmationEditText.text.toString(),
            username = phoneNumber!!
        )
        pBarCodeConfirm.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val response = registerViewModel.loginWithPwd(model)

            if (response.isSuccessful) {
                val resp = response.body()!!
                val edit1 =
                    requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                        .edit()
                edit1.putString(USER_PHONE, phoneNumber)
                edit1.putString(USER_PWD, model.password)
                edit1.apply()

                val edit2 =
                    requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE).edit()
                edit2.putInt(USER_ID_KEY, resp.id)
                edit2.putString(USER_ACCESS_TOKEN, resp.accessToken)
                edit2.putString(USER_CHAT_TOKEN, resp.chatToken)
                edit2.putString(USER_CHAT_EXP, resp.tokenExpirationTime)
                edit2.putString(USER_REFRESH_EXP, resp.refreshExpirationTime)
                edit2.putString(USER_REFRESH_TOKEN, resp.refreshToken)
                edit2.apply()

                findNavController().navigate(R.id.action_loginLoginFragment_to_pinOneFragment)
            }
        }
    }

}