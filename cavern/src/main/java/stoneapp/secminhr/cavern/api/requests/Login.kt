package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import java.net.HttpURLConnection
import java.net.URL

class Login(val username: String, private val password: String): Result<Boolean> {

    override suspend fun get(): Boolean {
        if(username == "") {
            return false
        }
        if(password == "") {
            return false
        }

        return withContext(Dispatchers.IO) {
            val connection = runCatching {
                val url = URL("${Cavern.host}/login.php")
                url.openConnection() as HttpURLConnection
            }.getOrThrow()

            connection.instanceFollowRedirects = false
            connection.requestMethod = "POST"
            val data = "username=$username&password=$password".toByteArray()
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.addRequestProperty("Content-Length", data.size.toString())
            connection.doOutput = true

            connection.runCatching {
                outputStream.write(data)

                if (responseCode == 302) {
                    val location = getHeaderField("location")
                    location?.contains("ok") ?: false
                } else {
                    false
                }
            }.getOrThrow()
        }
    }
}