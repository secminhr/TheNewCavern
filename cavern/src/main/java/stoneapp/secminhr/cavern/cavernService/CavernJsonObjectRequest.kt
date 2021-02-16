package stoneapp.secminhr.cavern.cavernService

import androidx.databinding.ObservableInt
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.*
import org.json.JSONObject
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernObject.Role
import stoneapp.secminhr.cavern.getBooleanFromKey
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getStringFromKey
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

open class CavernJsonObjectRequest(method: Int,
                                   url: String,
                                   val listener: Listener<JsonObject>,
                                   errorListener: ErrorListener? = null):
    JsonObjectRequest(method, url, null, JSONListener(listener), errorListener) {

    override fun getHeaders() = super.getHeaders() + XSRfHeader(XSRFTokenGenerator.token)
}

private class JSONListener(val listener: Listener<JsonObject>): Listener<JSONObject> {
    override fun onResponse(response: JSONObject?) {
        listener.onResponse(
            if (response != null) {
                gson.fromJson(response.toString(), JsonObject::class.java)
            } else {
                null
            }
        )
    }
}

val gson: Gson = gsonBuilder().create()

private fun gsonBuilder(): GsonBuilder {
    return GsonBuilder()
            .registerTypeAdapter(ArticlePreview::class.java, ArticlePreviewDeserializer)
            .registerTypeAdapter(Role::class.java, RoleDeserializer)
}

private object ArticlePreviewDeserializer: JsonDeserializer<ArticlePreview> {
    override fun deserialize(element: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ArticlePreview {
        val json = element?.asJsonObject ?: return ArticlePreview.empty

        val title = adjustContent(json.getStringFromKey("title_key"))
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val dateString =  json.getStringFromKey("date_key")
        val date = dateFormat.parse(dateString)!!
        val likes = json.getStringFromKey("upvote_key").toInt()
        val id = json.getIntFromKey("pid_key")
        val author = json.getStringFromKey("author_key")
        val liked = json.getBooleanFromKey("is_liked")
        val authorUsername = json.getStringFromKey("author_username_key")
        return ArticlePreview(title, author, authorUsername, date, ObservableInt(likes), id, liked)
    }

    private fun adjustContent(content: String): String {
        var adjusted = content.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("\\/", "/")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&amp", "&")
        val regex = Regex("@[a-zA-z0-9]+")
        adjusted = regex.replace(adjusted) {
            it.value + "@"
        }
        return adjusted
    }
}
private object RoleDeserializer: JsonDeserializer<Role> {
    override fun deserialize(element: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Role {
        val json = element?.asJsonObject ?: return Role.Empty
        return Role(json["name"].asString, json["canPostArticle"].asBoolean,
                json["canLike"].asBoolean,
                json["canComment"].asBoolean)
    }

}