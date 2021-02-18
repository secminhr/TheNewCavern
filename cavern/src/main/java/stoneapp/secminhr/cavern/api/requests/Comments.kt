package stoneapp.secminhr.cavern.api.requests

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.cavernService.gson
import java.net.URL
import java.net.UnknownHostException

suspend fun Comments(id: Int): List<Comment> = withContext(Dispatchers.IO) {

    val response  = runCatching {
        val url = URL("${Cavern.host}/ajax/comment.php?pid=$id")
        url.openStream().inputAs<CommentResponse>()
    }.getOrElse {
        Log.e("Comments", it.message!!)
        throw when (it) {
            is UnknownHostException -> NoConnectionError()
            else -> NetworkError()
        }
    }
    if (response.comments.size() > 0) {
        val nicknames = response.names.asJsonObject
        val hashes = response.hash.asJsonObject

        response.comments.map {
            val commentData = gson.fromJson(it, CommentData::class.java)
            val nickname = nicknames[commentData.username].asString
            val hash = hashes[commentData.username].asString
            Comment(commentData.id, commentData.username, nickname, commentData.markdown,
                "https://www.gravatar.com/avatar/$hash?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500")
        }
    } else {
       listOf()
    }
}

data class CommentResponse(val comments: JsonArray, val names: JsonElement, val hash: JsonElement)
data class CommentData(
    val id: String,
    val username: String,
    val markdown: String
)