package stoneapp.secminhr.cavern.api.requests

import com.google.gson.JsonObject
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import stoneapp.secminhr.cavern.cavernService.gson
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal fun InputStream.inputAsJson(): JsonObject = gson.fromJson(reader(), JsonObject::class.java)
internal inline fun<reified T> InputStream.inputAs(): T {
    val str = reader().readText()
    return gson.fromJson(str, T::class.java)
}
//reader().readText().let{ gson.fromJson<>(it,T::class.java)}

internal fun URL.openConnectionXSRF(): HttpURLConnection {
    val connection = openConnection() as HttpURLConnection
    val (key, value) = XSRfHeader(XSRFTokenGenerator.token)
    connection.setRequestProperty(key, value)
    return connection
}