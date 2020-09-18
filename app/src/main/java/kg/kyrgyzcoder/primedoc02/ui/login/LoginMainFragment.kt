package kg.kyrgyzcoder.primedoc02.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.data.login.ModelRegister
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc02.util.EXTRA_CODE_TYPE
import kg.kyrgyzcoder.primedoc02.util.EXTRA_USER_PHONE
import kg.kyrgyzcoder.primedoc02.util.EXTRA_USER_PWD

import kotlinx.android.synthetic.main.fragment_login_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException


class LoginMainFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ccp.registerCarrierNumberEditText(phoneNumberEditText)

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        registerButton.setOnClickListener {
            if (checkPhoneNumber())
                registerWithPhoneNumber()
        }

    }

    private fun registerWithPhoneNumber() {
        val phone = ccp.fullNumberWithPlus
        val pwd = getDeviceId(requireContext())
        val model = ModelRegister(listOf("USER"), pwd, phone)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                pBarLoginMain.visibility = View.VISIBLE
                val response = registerViewModel.registerNewUser(model)
                if (response.isSuccessful) {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_USER_PHONE, phone)
                    bundle.putString(EXTRA_USER_PWD, pwd)
                    bundle.putString(EXTRA_CODE_TYPE, "reg")
                    findNavController().navigate(
                        R.id.action_loginMainFragment_to_codeConfirmationFragment,
                        bundle
                    )
                } else {
                    val response2 = registerViewModel.recoverPwd(phone)
                    if (response2.isSuccessful) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_USER_PHONE, phone)
                        bundle.putString(EXTRA_USER_PWD, pwd)
                        bundle.putString(EXTRA_CODE_TYPE, "rec")
                        findNavController().navigate(
                            R.id.action_loginMainFragment_to_codeConfirmationFragment,
                            bundle
                        )
                    }
                }

            } catch (e: ConnectException) {

            } catch (e: SocketTimeoutException) {

            } catch (e: EOFException) {
                val bundle = Bundle()
                bundle.putString(EXTRA_USER_PHONE, phone)
                bundle.putString(EXTRA_USER_PWD, pwd)
                findNavController().navigate(
                    R.id.action_loginMainFragment_to_codeConfirmationFragment,
                    bundle
                )
            }
        }

    }

    private fun checkPhoneNumber(): Boolean {
        if (phoneNumberEditText.text.toString().length < 9) {
            phoneNumberEditText.error = getString(R.string.phoneNumberFormatError)
            return false
        }
        return true
    }

    // Device hard-coded id
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


}