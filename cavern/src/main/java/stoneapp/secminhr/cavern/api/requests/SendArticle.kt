package stoneapp.secminhr.cavern.api.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

suspend fun SendArticle(pid: Int = -1, title: String, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/post.php")
        url.openConnection() as HttpURLConnection
    }.getOrElse {
        return@withContext false
    }.apply {
        val header = XSRfHeader(XSRFTokenGenerator.token)

        requestMethod = "POST"
        instanceFollowRedirects = false
        data = "title=$title&content=$content&pid=$pid".toByteArray()
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        addRequestProperty("Content-Length", data.size.toString())
        setRequestProperty(header.first, header.second)
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