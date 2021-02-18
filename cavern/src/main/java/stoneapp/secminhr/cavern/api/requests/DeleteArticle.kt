package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

suspend fun DeleteArticle(id: Int): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("https://stoneapp.tech/cavern/post.php?del=$id")
        url.openConnection() as HttpURLConnection
    }.getOrElse {
        return@withContext false
    }.runCatching {
        val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
        setRequestProperty(key, value)
        connect()
        this
    }.getOrElse {
        return@withContext false
    }.run {
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            val location = getHeaderField("axios-location")
            location?.contains("ok") ?: false
        } else {
            false
        }
    }

}