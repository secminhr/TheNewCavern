package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import java.net.HttpURLConnection
import java.net.URL

suspend fun Login(username: String, password: String): Boolean {
    if(username == "") {
        return false
    }
    if(password == "") {
        return false
    }

    return withContext(Dispatchers.IO) {
        val data: ByteArray
        runCatching {
            val url = URL("${Cavern.host}/login.php")
            url.openConnection() as HttpURLConnection
        }.getOrElse {
            return@withContext false
        }.apply {
            instanceFollowRedirects = false
            requestMethod = "POST"
            data = "username=$username&password=$password".toByteArray()
            addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            addRequestProperty("Content-Length", data.size.toString())
            doOutput = true
        }.runCatching {
            outputStream.write(data)
            this
        }.getOrElse {
            return@withContext false
        }.runCatching {
            getHeaderField("Location")?.let {
                if (it.contains("index.php")) {
                    //already logged in
                    return@withContext true
                }
            }
            if (responseCode == 302) {
                val location = getHeaderField("location")
                location?.contains("ok") ?: false
            } else {
                false
            }
        }.getOrElse { false }
    }
}