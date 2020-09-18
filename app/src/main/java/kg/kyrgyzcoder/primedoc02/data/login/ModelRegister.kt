package kg.kyrgyzcoder.primedoc02.data.login


import com.google.gson.annotations.SerializedName

data class ModelRegister(
    val authorities: List<String>,
    val password: String,
    val username: String
)