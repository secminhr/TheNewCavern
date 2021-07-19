package personal.secminhr.cavern.main.ui.views.articleContent

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.MainActivity
import personal.secminhr.cavern.main.MainActivity.Companion.articleScreen
import personal.secminhr.cavern.main.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.main.mainActivity
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.ArticleContentViewModel
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
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                title = { Text("Delete?")},
                text = { Text("Delete this post?\nThere is no way to restore after deletion") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteArticle(preview.id) {
                            backToPreviousScreen()
                        }
                        showDeleteAlert.value = false
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAlert.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    private val editIcon = @Composable {
        Icon(Icons.Default.Edit, "Edit")
    }

    private val deleteIcon = @Composable {
        Icon(Icons.Default.DeleteForever, "Delete")
    }

    private fun deleteAction() {
        showDeleteAlert.value = true
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
            mutableListOf(::deleteAction, { editAction(preview.id) }) + super.sameAppBarIconActionAs(articleScreen)
        else
            super.sameAppBarIconActionAs(articleScreen)

    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")
    override val shouldShowBackButton = true

}