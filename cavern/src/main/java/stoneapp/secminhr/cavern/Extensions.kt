package stoneapp.secminhr.cavern

import org.json.JSONArray
import org.json.JSONObject
import stoneapp.secminhr.cavern.api.getStringFromMap

fun JSONObject.getStringFromKey(key: String): String = this.getString(getStringFromMap(key))
fun JSONObject.getIntFromKey(key: String) = getInt(getStringFromMap(key))
fun JSONObject.getJSONArrayFromKey(key: String): JSONArray = getJSONArray(getStringFromMap(key))
fun JSONObject.getBooleanFromKey(key: String): Boolean = getBoolean(getStringFromMap(key))
fun JSONObject.getJSONObjectFromKey(key: String): JSONObject = getJSONObject(getStringFromMap(key))
fun JSONObject.getFromKey(key: String): Any = get(getStringFromMap(key))

fun String?.isNotNullNorEmpty() = !this.isNullOrEmpty()

fun IntRange.collideWith(range: IntRange): Boolean {
    for(num in this) {
        if(range.contains(num)) {
            return true
        }
    }
    return false
}