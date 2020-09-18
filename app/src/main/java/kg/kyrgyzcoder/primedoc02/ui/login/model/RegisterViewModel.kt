package kg.kyrgyzcoder.primedoc02.ui.login.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc02.data.login.*
import kg.kyrgyzcoder.primedoc02.data.login.repository.RegisterRepository
import retrofit2.Response

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister> {
        return registerRepository.registerNewUser(modelRegister)
    }

    suspend fun verifyPhoneNumber(token: String): Response<ModelRegister> {
        return registerRepository.verifyCode(token)
    }

    suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse> {
        return registerRepository.loginWithPwd(model)
    }

    suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse> {
        return registerRepository.recoverPwd(phone)
    }

    suspend fun resetPwd(token: String): Response<ModelResetResponse> {
        return registerRepository.resetPwd(token)
    }

}
