package stoneapp.secminhr.cavern.cavernObject

import com.google.gson.annotations.SerializedName
import java.util.*


data class ArticlePreview(
    val title: String,
    @SerializedName("name") val author: String,
    @SerializedName("author") val authorUsername: String,
    @SerializedName("time") val date: Date,
    @SerializedName("likes_count") var upvote: String,
    @SerializedName("pid") val id: Int,
    @SerializedName("islike") var liked: Boolean
)
