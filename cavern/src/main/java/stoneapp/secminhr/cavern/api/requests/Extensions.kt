package stoneapp.secminhr.cavern.api.requests

import kotlinx.serialization.decodeFromString
import stoneapp.secminhr.cavern.api.Cavern.Companion.json
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal inline fun<reified T> InputStream.inputAs(): T = json.decodeFromString(reader().readText())

internal fun URL.openConnectionXSRF(): HttpURLConnection {
    val connection = openConnection() as HttpURLConnection
    val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
    connection.setRequestProperty(key, value)
    return connection
}