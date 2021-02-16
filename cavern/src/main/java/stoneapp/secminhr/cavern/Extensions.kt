package stoneapp.secminhr.cavern

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import stoneapp.secminhr.cavern.api.getStringFromMap

fun JsonObject.getStringFromKey(key: String): String = this[getStringFromMap(key)].asString
fun JsonObject.getIntFromKey(key: String) = this[getStringFromMap(key)].asInt
fun JsonObject.getJsonArrayFromKey(key: String): JsonArray = this[getStringFromMap(key)].asJsonArray
fun JsonObject.getBooleanFromKey(key: String) = this[getStringFromMap(key)].asBoolean
fun JsonObject.getJsonObjectFromKey(key: String): JsonObject = this[getStringFromMap(key)].asJsonObject
fun JsonObject.getFromKey(key: String): Any = this[getStringFromMap(key)]