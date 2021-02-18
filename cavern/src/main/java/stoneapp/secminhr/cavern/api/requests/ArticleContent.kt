package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import java.net.URL

suspend fun ArticleContent(preview: ArticlePreview): Article = withContext(Dispatchers.IO) {
    val json = runCatching {
        val url = URL("${Cavern.host}/ajax/posts.php?pid=${preview.id}")
        url.openStream().inputAsJson()
    }.getOrThrow()

    val content = json.getAsJsonObject("post")["content"].asString
    Article(preview, content)
}