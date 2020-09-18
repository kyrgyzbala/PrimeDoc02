package kg.kyrgyzcoder.primedoc02.data.login


import com.google.gson.annotations.SerializedName

data class ModelLoginResponse(
    val accessToken: String,
    val chatToken: String,
    val id: Int,
    val refreshExpirationTime: String,
    val refreshToken: String,
    val tokenExpirationTime: String
)