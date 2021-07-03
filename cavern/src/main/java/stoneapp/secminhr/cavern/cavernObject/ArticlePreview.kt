package stoneapp.secminhr.cavern.cavernObject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import stoneapp.secminhr.cavern.cavernService.DateSerializer
import java.util.*

@Serializable
data class ArticlePreview(
    val title: String,
    @SerialName("name") val author: String,
    @SerialName("author") val authorUsername: String,
    @SerialName("time") @Serializable(with = DateSerializer::class) val date: Date,
    @SerialName("likes_count") var upvote: String,
    @SerialName("pid") val id: Int,
    @SerialName("islike") var liked: Boolean
)
