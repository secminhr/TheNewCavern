package personal.secminhr.cavern.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class ArticleContentViewModel: CavernViewModel() {

    private var content = mutableStateOf(Article.empty)
    private var comments = mutableStateOf(listOf<Comment>())

    fun getArticleContent(preview: ArticlePreview, onError: (CavernError) -> Unit = {}): State<Article> {
        content.value = Article(preview.id, preview.title, preview.author, preview.authorUsername, preview.liked, "")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                content.value = cavernApi.getArticleContent(preview)
            } catch (e: CavernError) {
                onError(e)
            }
        }

        return content
    }

    fun getComments(preview: ArticlePreview, onError: (CavernError) -> Unit = {}): MutableState<List<Comment>> {
        comments.value = listOf()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                comments.value = cavernApi.getComments(preview.id)
            } catch (e: CavernError) {
                onError(e)
            }
        }

        return comments
    }


    fun sendComment(pid: Int, content: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = cavernApi.sendComment(pid, content)
            if (success) {
                onSuccess()
            }
        }

    }

}