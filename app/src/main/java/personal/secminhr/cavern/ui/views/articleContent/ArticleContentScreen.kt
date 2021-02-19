package personal.secminhr.cavern.ui.views.articleContent

import android.widget.Toast
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.MainActivity.Companion.articleScreen
import personal.secminhr.cavern.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.mainActivity
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.viewmodel.ArticleContentViewModel
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(preview: ArticlePreview): Screen {

    lateinit var viewModel: ArticleContentViewModel
    lateinit var article: State<Article>
    private val showDeleteAlert = mutableStateOf(false)

    @ExperimentalMaterialApi
    override val content = @Composable {
        viewModel = viewModel()
        var comments = viewModel.getComments(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }
        article = viewModel.getArticleContent(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }
        ArticleContentView(
            article = article,
            titleState = topBarTitle,
            preview = preview,
            comments = comments,
            onCommentSend = {
                comments = viewModel.getComments(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }
            }
        )
        if (showDeleteAlert.value) {
            AlertDialog(
                onDismissRequest = { showDeleteAlert.value = false },
                title = { Text("Delete?")},
                text = { Text("Delete this post?\nThere is no way to restore after deletion") },
                confirmButton = { }
            )
        }
    }

    private val editIcon = @Composable {
        Icon(Icons.Default.Edit, "Edit")
    }

    private val deleteIcon = @Composable {
        Icon(Icons.Default.DeleteForever, "Delete")
    }

    private fun deleteAction(id: Int) {
        showDeleteAlert.value = true
        viewModel.deleteArticle(id) {
            backToPreviousScreen()
        }
    }

    private fun editAction(id: Int) {
        navigateTo(editorScreen(article.value.title, article.value.content, id))
    }

    override val topBarIcons: List<@Composable () -> Unit> =
        if (MainActivity.hasCurrentUser && MainActivity.currentAccount!!.username == preview.authorUsername) {
            listOf(deleteIcon, editIcon, super.sameAppBarIconAs(articleScreen)[0])
        } else {
            super.sameAppBarIconAs(articleScreen)
        }

    override val topBarIconActions: List<() -> Unit> =
        if (MainActivity.hasCurrentUser && MainActivity.currentAccount!!.username == preview.authorUsername)
            mutableListOf({ deleteAction(preview.id) }, { editAction(preview.id) }) + super.sameAppBarIconActionAs(articleScreen)
        else
            super.sameAppBarIconActionAs(articleScreen)

    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")
    override val shouldShowBackButton = true

}