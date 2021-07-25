package personal.secminhr.cavern.main.ui.views.articleContent

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import personal.secminhr.cavern.main.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.ui.views.articles.ArticleScreen
import personal.secminhr.cavern.main.ui.views.login.UserView
import personal.secminhr.cavern.main.viewmodel.ArticleViewModel
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(val preview: ArticlePreview): Screen() {

    @ExperimentalMaterialApi
    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        val viewModel = viewModel<ArticleViewModel>()
        val userViewModel = viewModel<CurrentUserViewModel>()

        val comments by remember { viewModel.getComments(preview) { showSnackbar(it.message!!) } }
        val article by remember { viewModel.getArticleContent(preview) { showSnackbar(it.message!!) } }
        var showDeleteAlert by remember { mutableStateOf(false) }
        val title = remember { mutableStateOf("Cavern") }

        appBar(showBackButton = true) {
            title(title.value)
            if (preview.authorIs(userViewModel.currentUser)) {
                iconButton({ showDeleteAlert = true }) {
                    Icon(Icons.Default.DeleteForever, "Delete")
                }
                iconButton({ navigateTo(editorScreen(article.title, article.content, article.id)) }) {
                    Icon(Icons.Default.Edit, "Edit")
                }
            }
            iconsFrom(ArticleScreen)
        }

        if (showDeleteAlert) {
            DeleteAlertDialog(
                onDeleteRequest = {
                    viewModel.deleteArticle(preview.id) {
                        backToPreviousScreen()
                    }
                    showDeleteAlert = false
                },
                onDismiss = {
                    showDeleteAlert = false
                }
            )
        }

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        var bottomSheetUsername by remember { mutableStateOf(preview.authorUsername) }
        val scope = rememberCoroutineScope()

        BottomSheetScaffold(sheetContent = {
            UserView(bottomSheetUsername, showSnackbar)
        }, scaffoldState = bottomSheetScaffoldState, sheetPeekHeight = 0.dp) {
            Column {
                var comment by remember { mutableStateOf("") }
                ArticleContentView(
                    article = article,
                    titleState = title,
                    comments = comments,
                    onShowBottomRequest = {
                        bottomSheetUsername = it
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    },
                    onReplyClicked = {
                        comment += "@$it "
                    },
                    modifier = Modifier.weight(1f)
                )

                if (userViewModel.currentUser != null) {
                    CommentEdit(
                        comment = comment,
                        onCommentChange = {
                            comment = it
                        },
                        onSendButtonClick = {
                            viewModel.sendComment(preview.id, comment) {
                                viewModel.getComments(preview) { showSnackbar(it.message!!) }
                                comment = ""
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAlertDialog(onDeleteRequest: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        title = { Text("Delete?")},
        text = { Text("Delete this post?\nThere is no way to restore after deletion") },
        confirmButton = {
            TextButton(onClick = onDeleteRequest) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CommentEdit(
    comment: String,
    onCommentChange: (String) -> Unit,
    onSendButtonClick: () -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        Divider()
        val interactionSource = remember { MutableInteractionSource() }
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            label = { Text("Your new comment") },
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp).align(Alignment.CenterHorizontally).fillMaxWidth(),
            interactionSource = interactionSource,
            trailingIcon = {
                IconButton(onClick = onSendButtonClick) {
                    Icon(Icons.Default.Send, "Send comment")
                }
            }
        )
    }
}

private fun ArticlePreview.authorIs(user: Account?): Boolean = authorUsername == user?.username