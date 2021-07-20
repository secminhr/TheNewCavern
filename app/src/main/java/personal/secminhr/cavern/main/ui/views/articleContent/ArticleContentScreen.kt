package personal.secminhr.cavern.main.ui.views.articleContent

import androidx.compose.foundation.layout.RowScope
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
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.ArticleContentViewModel
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(val preview: ArticlePreview): Screen {

    lateinit var viewModel: ArticleContentViewModel
    lateinit var article: State<Article>
    private val showDeleteAlert = mutableStateOf(false)

    @ExperimentalMaterialApi
    @Composable
    override fun Content(showSnackbar: (String) -> Unit) {
        viewModel = viewModel()
        var comments = viewModel.getComments(preview) { showSnackbar(it.message!!) }
        article = viewModel.getArticleContent(preview) { showSnackbar(it.message!!) }
        ArticleContentView(
            article = article,
            titleState = topBarTitle,
            preview = preview,
            comments = comments,
            onCommentSend = {
                comments = viewModel.getComments(preview) { showSnackbar(it.message!!) }
            },
            showSnackbar = showSnackbar
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

    private fun delete() {
        showDeleteAlert.value = true
    }

    private fun edit(id: Int) {
        navigateTo(editorScreen(article.value.title, article.value.content, id))
    }

    override val topBarIcons: @Composable RowScope.() -> Unit = {
        if (MainActivity.hasCurrentUser && MainActivity.currentAccount!!.username == preview.authorUsername) {
            IconButton(onClick = { delete() }) {
                Icon(Icons.Default.DeleteForever, "Delete")
            }
            IconButton(onClick = { edit(preview.id) }) {
                Icon(Icons.Default.Edit, "Edit")
            }
        }
        super.sameAppBarIconAs(articleScreen)(this)
    }

    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")
    override val shouldShowBackButton = true

}