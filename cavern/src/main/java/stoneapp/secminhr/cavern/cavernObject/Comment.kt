package stoneapp.secminhr.cavern.cavernObject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    @SerialName("username") val commenterUsername: String,
    val commenterNickname: String,
    val content: String,
    val imageUrl: String
)