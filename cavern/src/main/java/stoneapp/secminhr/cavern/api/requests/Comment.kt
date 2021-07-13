package stoneapp.secminhr.cavern.api.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.Cavern.Companion.json
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Comment
import java.net.URL
import java.net.UnknownHostException

internal suspend fun Comments(id: Int): List<Comment> = withContext(Dispatchers.IO) {

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
    response.comments.map {
        val commentData = json.decodeFromJsonElement<CommentData>(it)
        val nickname = response.names.jsonObject[commentData.username]?.jsonPrimitive?.content ?: ""
        val hash = response.hash.jsonObject[commentData.username]?.jsonPrimitive?.content ?: ""
        Comment(commentData.id, commentData.username, nickname, commentData.markdown,
            "https://www.gravatar.com/avatar/$hash?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500")
    }
}

@Serializable
private data class CommentResponse(val comments: JsonArray, val names: JsonElement, val hash: JsonElement)
@Serializable
private data class CommentData(
    val id: String,
    val username: String,
    val markdown: String
)

internal suspend fun EditComment(id: Int, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/ajax/comment.php")
        url.openConnectionXSRF()
    }.getOrElse {
        return@withContext false
    }.apply {
        requestMethod = "POST"
        data = "edit=$id&content=$content".toByteArray()
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        addRequestProperty("Content-Length", data.size.toString())
        doOutput = true
        doInput = true
    }.runCatching {
        outputStream.write(data)
        inputStream.inputAs<JsonObject>()
    }.getOrElse {
        return@withContext false
    }.run {
        this["status"]?.jsonPrimitive?.boolean ?: false
    }
}

internal suspend fun SendComment(pid: Int, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/ajax/comment.php")
        url.openConnectionXSRF()
    }.getOrElse {
        return@withContext false
    }.apply {
        instanceFollowRedirects = false
        requestMethod = "POST"
        data = "pid=$pid&content=$content".toByteArray()
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        addRequestProperty("Content-Length", data.size.toString())
        doOutput = true
        doInput = true
    }.runCatching {
        outputStream.write(data)
        inputStream.inputAs<JsonObject>()
    }.getOrElse {
        return@withContext false
    }.run {
        this["status"]?.jsonPrimitive?.boolean ?: false
    }
}