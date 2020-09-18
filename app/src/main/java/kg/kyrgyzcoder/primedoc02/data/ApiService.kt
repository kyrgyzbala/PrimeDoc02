package kg.kyrgyzcoder.primedoc02.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kg.kyrgyzcoder.primedoc02.data.login.*
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("v1/register")
    fun registerAsync(@Body model: ModelRegister): Deferred<Response<ModelRegister>>

    @POST("v1/verify/{token}")
    fun verifyCodeAsync(@Path("token") token: String): Deferred<Response<ModelRegister>>

    @POST("v1/auth")
    fun loginWithPwdAsync(@Body model: ModelLogin): Deferred<Response<ModelLoginResponse>>

    @POST("v1/recovery/{phone}")
    fun recoverPwdAsync(@Path("phone") phoneNumber: String): Deferred<Response<ModelRecoveryResponse>>

    @POST("v1/reset/{token}")
    fun resetPwdAsync(@Path("token") token: String): Deferred<Response<ModelResetResponse>>



    companion object {
        operator fun invoke(interceptor: ConnectivityInterceptorImpl): ApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://165.22.74.215:8080/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}