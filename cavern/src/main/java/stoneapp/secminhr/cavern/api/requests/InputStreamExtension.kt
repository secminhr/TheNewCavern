package stoneapp.secminhr.cavern.api.requests

import com.google.gson.JsonObject
import stoneapp.secminhr.cavern.cavernService.gson
import java.io.InputStream

fun InputStream.inputAsJson(): JsonObject = gson.fromJson(reader(), JsonObject::class.java)
inline fun<reified T> InputStream.inputAs(): T {
    val str = reader().readText()
    return gson.fromJson(str, T::class.java)
}