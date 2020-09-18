package kg.kyrgyzcoder.primedoc02.data.login.repository

import kg.kyrgyzcoder.primedoc02.data.login.*
import retrofit2.Response

interface RegisterRepository {

    suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister>
    suspend fun verifyCode(token: String): Response<ModelRegister>

    suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse>
    suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse>
    suspend fun resetPwd(token: String): Response<ModelResetResponse>
}