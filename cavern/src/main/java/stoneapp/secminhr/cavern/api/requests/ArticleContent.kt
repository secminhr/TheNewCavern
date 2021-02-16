package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.getJsonObjectFromKey
import stoneapp.secminhr.cavern.getStringFromKey
import java.net.URL

class ArticleContent(private val preview: ArticlePreview): Result<Article> {
    override suspend fun get(): Article = withContext(Dispatchers.IO) {
        val json = runCatching {
            val url = URL("${Cavern.host}/ajax/posts.php?pid=${preview.id}")
            url.openStream().inputAsJson()
        }.getOrThrow()

        val content = json.getJsonObjectFromKey("post_key").getStringFromKey("content_key")
        Article(preview, content)
    }
}