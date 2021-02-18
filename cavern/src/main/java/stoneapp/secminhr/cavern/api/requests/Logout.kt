package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.HttpURLConnection
import java.net.URL

suspend fun Logout(): Boolean = withContext(Dispatchers.IO) {
    val connection = runCatching {
        val url = URL("${Cavern.host}/login.php?logout")
        val c = url.openConnection() as HttpURLConnection
        val header = XSRfHeader(XSRFTokenGenerator.token)
        c.setRequestProperty(header.first, header.second)
        c.connect()
        c
    }.getOrElse {
        return@withContext false
    }

    connection.responseCode == HttpURLConnection.HTTP_OK
}