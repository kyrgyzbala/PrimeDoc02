package kg.kyrgyzcoder.primedoc02.data.login.repository

import kg.kyrgyzcoder.primedoc02.data.ApiService
import kg.kyrgyzcoder.primedoc02.data.login.*
import kg.kyrgyzcoder.primedoc02.data.login.repository.RegisterRepository
import retrofit2.Response

class RegisterRepositoryImpl(private val apiService: ApiService) : RegisterRepository {

    override suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister> {
        return apiService.registerAsync(modelRegister).await()
    }

    override suspend fun verifyCode(token: String): Response<ModelRegister> {
        return apiService.verifyCodeAsync(token).await()
    }

    override suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse> {
        return apiService.loginWithPwdAsync(model).await()
    }

    override suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse> {
        return apiService.recoverPwdAsync(phone).await()
    }

    override suspend fun resetPwd(token: String): Response<ModelResetResponse> {
        return apiService.resetPwdAsync(token).await()
    }
}