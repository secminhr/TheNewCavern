package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

suspend fun SendComment(pid: Int, content: String): Boolean = withContext(Dispatchers.IO) {
    val data: ByteArray
    runCatching {
        val url = URL("${Cavern.host}/ajax/comment.php")
        url.openConnection() as HttpURLConnection
    }.getOrElse {
        return@withContext false
    }.apply {
        instanceFollowRedirects = false
        requestMethod = "POST"
        val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
        data = "pid=$pid&content=$content".toByteArray()
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        addRequestProperty("Content-Length", data.size.toString())
        setRequestProperty(key, value)
        doOutput = true
        doInput = true
    }.runCatching {
        outputStream.write(data)
        inputStream.inputAsJson()
    }.getOrElse {
        return@withContext false
    }.run {
        this["status"].asBoolean
    }
}