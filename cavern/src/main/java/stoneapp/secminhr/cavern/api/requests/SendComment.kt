package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import stoneapp.secminhr.cavern.getBooleanFromKey
import stoneapp.secminhr.cavern.getIntFromKey
import java.net.HttpURLConnection
import java.net.URL

class SendComment(
    private val pid: Int,
    private val content: String,
    private val sender: Account
): Result<Comment?> {

    override suspend fun get(): Comment? = withContext(Dispatchers.IO) {
        val connection = runCatching {
            val url = URL("${Cavern.host}/ajax/comment.php")
            url.openConnection() as HttpURLConnection
        }.getOrThrow()

        connection.instanceFollowRedirects = false
        connection.requestMethod = "POST"
        val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
        val data = "pid=$pid&content=$content".toByteArray()
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.addRequestProperty("Content-Length", data.size.toString())
        connection.setRequestProperty(key, value)
        connection.doOutput = true
        connection.doInput = true

        connection.runCatching {
            outputStream.write(data)
        }

        val response = connection.runCatching {
            inputStream.inputAsJson()
        }.getOrThrow()


        if (response.getBooleanFromKey("status_key")) {
            val id = response.getIntFromKey("comment_id_key")
            Comment(id, sender.username, sender.nickname, content, sender.avatarLink)
        } else {
            null
        }
    }
}