package stoneapp.secminhr.cavern.api.results

import com.android.volley.Request
import com.android.volley.RequestQueue
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.getJSONObjectFromKey
import stoneapp.secminhr.cavern.getStringFromKey

class ArticleContent(private val queue: RequestQueue, private val preview: ArticlePreview): CavernResult<ArticleContent> {

    lateinit var article: Article

    override fun get(onSuccess: (ArticleContent) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/ajax/posts.php?pid=${preview.id}"
        val request = CavernJsonObjectRequest(Request.Method.GET, url, null, {
            val content = it.getJSONObjectFromKey("post_key").getStringFromKey("content_key")
            article = Article(preview.id, preview.title, preview.author, preview.authorUsername, preview.liked, content)
            onSuccess(this)
        })
        queue.add(request)
    }
}