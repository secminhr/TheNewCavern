package stoneapp.secminhr.cavern.api.results

import androidx.databinding.ObservableInt
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import org.json.JSONArray
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.getBooleanFromKey
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getJSONArrayFromKey
import stoneapp.secminhr.cavern.getStringFromKey
import java.text.SimpleDateFormat
import java.util.*

class Articles(
    private val queue: RequestQueue,
    private val page: Int,
    private val limit: Int
): CavernResult<Articles> {

    val articles: MutableList<ArticlePreview> = mutableListOf()
    var totalPageCount: Int = 0

    override fun get(onSuccess: (Articles) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/ajax/posts.php?page=$page&limit=$limit"
        val request = CavernJsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { json ->
                    val total = json.getIntFromKey("total_posts_num")
                    totalPageCount = total
                    articles += parseJson(json.getJSONArrayFromKey("posts_key"))
                    onSuccess(this)
                }
        )
        queue.add(request)
    }

    private fun parseJson(jsonArray: JSONArray): Array<ArticlePreview> {
        var articleArr = arrayOf<ArticlePreview>()
        for(i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            val title = adjustContent(json.getStringFromKey("title_key"))
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val dateString =  json.getStringFromKey("date_key")
            val date = dateFormat.parse(dateString)
            val likes = json.getStringFromKey("upvote_key").toInt()
            val id = json.getIntFromKey("pid_key")
            val author = json.getStringFromKey("author_key")
            val liked = json.getBooleanFromKey("is_liked")
            val authorUsername = json.getStringFromKey("author_username_key")
            articleArr += ArticlePreview(title, author, authorUsername, date, ObservableInt(likes), id, liked)
        }
        return articleArr
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