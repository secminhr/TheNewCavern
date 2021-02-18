package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

suspend fun PublishArticle(title: String, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/post.php")
        url.openConnection() as HttpURLConnection
    }.getOrElse {
        return@withContext false
    }.apply {
        val header = XSRfHeader(XSRFTokenGenerator.token)

        requestMethod = "POST"
        data = "title=$title&content=$content&pid=-1".toByteArray()
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
        responseCode == HttpURLConnection.HTTP_CREATED
    }
}