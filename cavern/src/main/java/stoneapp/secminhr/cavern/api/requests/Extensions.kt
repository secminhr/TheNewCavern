package stoneapp.secminhr.cavern.api.requests

import kotlinx.serialization.decodeFromString
import stoneapp.secminhr.cavern.api.Cavern.Companion.json
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal inline fun<reified T> InputStream.inputAs(): T = json.decodeFromString(reader().readText())

internal fun URL.openConnectionXSRF(): HttpsURLConnection {
    val connection = openConnection() as HttpsURLConnection
    val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
    connection.setRequestProperty(key, value)
    return connection
}