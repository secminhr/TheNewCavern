package stoneapp.secminhr.cavern.api.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.Cavern.Companion.json
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import java.util.*

internal suspend fun ArticleContent(preview: ArticlePreview): Article = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("${Cavern.host}/ajax/posts.php?pid=${preview.id}")
        url.openStream().inputAs<JsonObject>()
    }.getOrElse {
        throw when(it) {
            is UnknownHostException -> NoConnectionError()
            else -> NetworkError()
        }
    }.run {
        val content = this["post"]?.jsonObject?.get("content")?.jsonPrimitive?.content ?: ""
        Article(preview, content)
    }
}

internal suspend fun DeleteArticle(id: Int): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("https://stoneapp.tech/cavern/post.php?del=$id")
        val connection = url.openConnectionXSRF()
        connection.connect()
        connection
    }.getOrElse {
        return@withContext false
    }.run {
        responseCode == HttpURLConnection.HTTP_NO_CONTENT &&
                (getHeaderField("axios-location")?.contains("ok") ?: false)
    }
}

internal suspend fun SendArticle(pid: Int = -1, title: String, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/post.php")
        url.openConnectionXSRF()
    }.getOrElse {
        return@withContext false
    }.apply {
        requestMethod = "POST"
        instanceFollowRedirects = false
        data = "title=$title&content=$content&pid=$pid".toByteArray()
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        addRequestProperty("Content-Length", data.size.toString())
        doOutput = true
    }.runCatching {
        outputStream.write(data)
        this
    }.getOrElse {
        return@withContext false
    }.run {
        Log.e("SendArticle", "respondeCode: $responseCode")
        responseCode == if(pid == -1) HttpURLConnection.HTTP_CREATED else HttpURLConnection.HTTP_OK
    }
}

internal suspend fun LikeArticle(id: Int): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("${Cavern.host}/ajax/like.php?pid=$id")
        url.openConnectionXSRF()
    }.getOrElse {
        return@withContext false
    }.runCatching {
        doOutput = true
        doInput = true
        this.inputStream.inputAs<JsonObject>()
    }.onFailure {
        return@withContext false
    }

    true
}