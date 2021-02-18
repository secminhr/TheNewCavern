package stoneapp.secminhr.cavern.cavernObject

import com.google.gson.annotations.SerializedName

data class Comment(
    val id: String,
    @SerializedName("username") val commenterUsername: String,
    val commenterNickname: String,
    val content: String,
    val imageUrl: String
)