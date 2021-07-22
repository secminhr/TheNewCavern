package personal.secminhr.cavern.main.ui.views.articleContent

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.MainActivity.Companion.articleScreen
import personal.secminhr.cavern.main.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.ArticleContentViewModel
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(val preview: ArticlePreview): Screen() {

    @ExperimentalMaterialApi
    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        val viewModel = viewModel<ArticleContentViewModel>()
        var comments by remember { viewModel.getComments(preview) { showSnackbar(it.message!!) } }
        val article by remember { viewModel.getArticleContent(preview) { showSnackbar(it.message!!) } }
        var showDeleteAlert by remember { mutableStateOf(false) }
        val userViewModel = viewModel<CurrentUserViewModel>()
        val title = remember{ mutableStateOf("Cavern") }

        appBar(showBackButton = true) {
            title(title.value)

            if (userViewModel.currentUser?.username == preview.authorUsername) {
                iconButton({ showDeleteAlert = true }) {
                    Icon(Icons.Default.DeleteForever, "Delete")
                }
                iconButton({ navigateTo(editorScreen(article.title, article.content, article.id)) }) {
                    Icon(Icons.Default.Edit, "Edit")
                }
            }
            iconsFrom(articleScreen)
        }

        ArticleContentView(
            article = article,
            titleState = title,
            preview = preview,
            comments = comments,
            onCommentSend = {
                viewModel.getComments(preview) { showSnackbar(it.message!!) }
            },
            showSnackbar = showSnackbar
        )
        if (showDeleteAlert) {
            AlertDialog(
                onDismissRequest = { showDeleteAlert = false },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                title = { Text("Delete?")},
                text = { Text("Delete this post?\nThere is no way to restore after deletion") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteArticle(preview.id) {
                            backToPreviousScreen()
                        }
                        showDeleteAlert = false
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAlert = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}