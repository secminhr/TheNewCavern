package stoneapp.secminhr.cavern.api.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.gson
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

internal suspend fun Articles(pageLimit: Int): Flow<Pair<Int, List<ArticlePreview>>> = flow {
    var counter = 1
    while (true) {
        val list = mutableListOf<ArticlePreview>()
        runCatching {
            val url = URL("${Cavern.host}/ajax/posts.php?page=$counter&limit=$pageLimit")
            url.openStream().inputAsJson()
        }.getOrElse {
            throw when (it) {
                is UnknownHostException -> NoConnectionError()
                else -> NetworkError()
            }
        }.run {
            list.addAll(gson.fromJson(
                getAsJsonArray("posts"),
                Array<ArticlePreview>::class.java
            ))
        }

        if (list.size <= 0) {
            counter = 1
            continue
        }

        emit(counter to list)
        counter++
    }
}.flowOn(Dispatchers.IO)

internal suspend fun ArticleContent(preview: ArticlePreview): Article = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("${Cavern.host}/ajax/posts.php?pid=${preview.id}")
        url.openStream().inputAsJson()
    }.getOrElse {
        throw when(it) {
            is UnknownHostException -> NoConnectionError()
            else -> NetworkError()
        }
    }.run {
        val content = getAsJsonObject("post")["content"].asString
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
        this.getInputStream().inputAsJson()
    }.onFailure {
        return@withContext false
    }

    true
}