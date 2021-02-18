package personal.secminhr.cavern.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class ArticleContentViewModel: CavernViewModel() {

    private var content = mutableStateOf(Article.empty)
    private var comments = mutableStateOf(listOf<Comment>())

    fun getArticleContent(preview: ArticlePreview): State<Article> {
        content.value = Article(preview.id, preview.title, preview.author, preview.authorUsername, preview.liked, "")
        viewModelScope.launch(Dispatchers.IO) {
            content.value = cavernApi.getArticleContent(preview)
        }

        return content
    }

    fun getComments(preview: ArticlePreview): MutableState<List<Comment>> {
        comments.value = listOf()
        viewModelScope.launch(Dispatchers.IO) {
            comments.value = cavernApi.getComments(preview.id)
        }

        return comments
    }


    fun sendComment(pid: Int, content: String, sender: Account, onSuccess: (Comment) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = cavernApi.sendComment(pid, content, sender)
            if (comment != null) {
                onSuccess(comment)
            }
        }

    }

}