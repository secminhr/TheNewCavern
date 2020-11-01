package stoneapp.secminhr.cavern.api.results

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response.Listener
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.getJSONArrayFromKey
import stoneapp.secminhr.cavern.getJSONObjectFromKey
import stoneapp.secminhr.cavern.getStringFromKey

class Comments(private val id: Int, private val queue: RequestQueue): CavernResult<Comments> {

    val comments = mutableListOf<Comment>()

    override fun get(onSuccess: (Comments) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/ajax/comment.php?pid=$id"
        val request = CavernJsonObjectRequest(Request.Method.GET, url, null,
                Listener {
                    val arr = it.getJSONArrayFromKey("comments_key")
                    if(arr.length() > 0) {
                        val nicknames = it.getJSONObjectFromKey("names_key")
                        val hashes = it.getJSONObjectFromKey("hash_key")
                        for (i in 0 until arr.length()) {
                            val comment = arr.getJSONObject(i)
                            val id = comment.getStringFromKey("id_key").toInt()
                            val username = comment.getStringFromKey("username_key")
                            val content = adjustContent(comment.getStringFromKey("markdown_key"))
                            val nickname = nicknames.getString(username)
                            val hash = hashes.getString(username)
                            comments += Comment(id, username,
                                    nickname,
                                    content,
                                    "https://www.gravatar.com/avatar/$hash?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500")
                        }
                    }
                    onSuccess(this)
                })
        queue.add(request)
    }

    private fun adjustContent(content: String): String {
        var adjusted = content.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("\\/", "/")
                .replace("&quot;", "\"")
        val regex = Regex("@[a-zA-z0-9]+")
        adjusted = regex.replace(adjusted) {
            it.value + "@"
        }
        return adjusted
    }
}