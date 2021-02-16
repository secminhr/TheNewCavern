package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

class PublishArticle(val title: String, val content: String): Result<Boolean> {

    override suspend fun get(): Boolean = withContext(Dispatchers.IO) {
        val connection = runCatching {
            val url = URL("${Cavern.host}/post.php")
            url.openConnection() as HttpURLConnection
        }.getOrThrow()

        val header = XSRfHeader(XSRFTokenGenerator.token)

        connection.requestMethod = "POST"
        val data = "title=$title&content=$content&pid=-1".toByteArray()
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.addRequestProperty("Content-Length", data.size.toString())
        connection.setRequestProperty(header.first, header.second)

        connection.doOutput = true

        runCatching {
            connection.outputStream.write(data)
        }

        connection.responseCode == HttpURLConnection.HTTP_CREATED
    }
}