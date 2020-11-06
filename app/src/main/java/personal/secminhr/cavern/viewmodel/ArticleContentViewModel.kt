package personal.secminhr.cavern.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel
import stoneapp.secminhr.coroutine.awaitArticleContent

class ArticleContentViewModel: CavernViewModel() {

    private var content: MutableState<Article> =  mutableStateOf(Article.empty)

    fun getArticleContent(preview: ArticlePreview): State<Article> {
        content.value = Article(preview.id, preview.title, preview.author, preview.authorUsername, preview.liked, "")
        viewModelScope.launch(Dispatchers.IO) {
            val result = cavernApi.awaitArticleContent(preview)
            content.value = result.article
        }
        return content
    }

}