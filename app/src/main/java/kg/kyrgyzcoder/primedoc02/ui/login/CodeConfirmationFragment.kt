package kg.kyrgyzcoder.primedoc02.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.data.login.ModelRegister
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc02.util.*

import kotlinx.android.synthetic.main.fragment_code_confirmation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CodeConfirmationFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        val bundle = arguments

        val phoneNumber = bundle?.getString(EXTRA_USER_PHONE)
        val pwd = bundle?.getString(EXTRA_USER_PWD)
        val type = bundle?.getString(EXTRA_CODE_TYPE)

        textViewCodeSentNumber.text = phoneNumber

        nextButtonCodeConfirm.setOnClickListener {
            if (codeConfirmationEditText.text.toString().isEmpty()) {
                codeConfirmationEditText.error = getString(R.string.requiredField)
            } else {
                if (type == "reg")
                    verifyCode(pwd, phoneNumber)
                else
                    resetPwd(phoneNumber)
            }
        }

        codeNotReceivedTextView.setOnClickListener {
            if (type == "reg")
                sendRegCode(phoneNumber!!)
            else
                sendRecCode(phoneNumber!!)
        }

    }

    private fun sendRecCode(phoneNumber: String) {

        GlobalScope.launch(Dispatchers.Main) {
            val response = registerViewModel.recoverPwd(phoneNumber)

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Код отправлен заново", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ошибка ${response.message()}, code: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun sendRegCode(phoneNumber: String) {
        val pwd = getDeviceId(requireContext())
        val model = ModelRegister(listOf("ADMIN"), pwd, phoneNumber)

        GlobalScope.launch(Dispatchers.Main) {
            val response = registerViewModel.registerNewUser(model)
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Код отправлен заново", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ошибка ${response.message()}, code: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Device hard-coded id
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun resetPwd(phoneNumber: String?) {
        pBarCodeConfirm.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val response = registerViewModel.resetPwd(codeConfirmationEditText.text.toString())
            pBarCodeConfirm.visibility = View.GONE

            if (response.isSuccessful) {
                val bundle = Bundle()
                bundle.putString(EXTRA_USER_PHONE, phoneNumber)
                findNavController().navigate(
                    R.id.action_codeConfirmationFragment_to_loginLoginFragment,
                    bundle
                )
            }
        }

    }

    private fun verifyCode(pwd: String?, phoneNumber: String?) {
        pBarCodeConfirm.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val response =
                registerViewModel.verifyPhoneNumber(codeConfirmationEditText.text.toString())

            pBarCodeConfirm.visibility = View.GONE

            if (response.isSuccessful) {
                val editorSp =
                    requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                        .edit()
                editorSp.putString(USER_PHONE, phoneNumber)
                editorSp.putString(USER_PWD, pwd)
                editorSp.apply()
                findNavController().navigate(R.id.action_codeConfirmationFragment_to_pinOneFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "WRONG CODE (text will be changed later)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}