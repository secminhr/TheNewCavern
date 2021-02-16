package stoneapp.secminhr.cavern.api.requests

import com.google.gson.JsonObject
import stoneapp.secminhr.cavern.cavernService.gson
import java.io.InputStream

interface Result<T> {
    suspend fun get(): T
}

fun InputStream.inputAsJson(): JsonObject{
    return gson.fromJson(reader(), JsonObject::class.java)
}
