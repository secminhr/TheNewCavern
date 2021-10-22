package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.EditorViewModel

data class EditModeSetting(val id: Int, val title: String, val content: String)

class EditorScreen(val editModeSetting: EditModeSetting? = null): Screen() {

    private val saved = mutableStateOf(true)
    private val showLeavingAlert = mutableStateOf(false)
    private val inEditArticleMode = (editModeSetting != null)

    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        val editorViewModel: EditorViewModel = viewModel(factory = EditorViewModel.factory(LocalContext.current))
        val savedTitle by editorViewModel.getTitle().collectAsState(null)
        val savedContent by editorViewModel.getContent().collectAsState(null)

        if (savedTitle == null || savedContent == null) {
            CircularProgressIndicator()
        } else {
            var title by remember {
                mutableStateOf(editModeSetting?.title ?: savedTitle!!)
            }
            var articleContent by remember {
                mutableStateOf(editModeSetting?.content?.let { TextFieldValue(text = it) } ?: savedContent!!)
            }

            var topBarTitle by remember { mutableStateOf("Cavern") }
            var isContentError by remember { mutableStateOf(false) }
            appBar(showBackButton = true) {
                title(topBarTitle)
                if (editModeSetting == null) {
                    iconButton({
                        save(editorViewModel, title, articleContent) { showSnackbar("Saved") }
                    }) {
                        Icon(Icons.Default.Save, "Save", tint = Color.White)
                    }
                }
                iconButton({
                    if (!inEditArticleMode) {
                        val contentIsEmpty = send(editorViewModel, title, articleContent.text)
                        isContentError = contentIsEmpty
                    } else {
                        val contentIsEmpty = edit(editorViewModel, title, articleContent.text)
                        isContentError = contentIsEmpty
                    }
                }) {
                    SendIcon()
                }
            }

            if (showLeavingAlert.value && !inEditArticleMode) {
                AlertDialog(onDismissRequest = {},
                    title = { Text("Saving?") },
                    text = { Text("Your draft hasn't been saved.\nDo you want to save it?") },
                    confirmButton = {
                        Button(onClick = {
                            editorViewModel.save(title, articleContent) {
                                showSnackbar("Saved!")
                            }
                            saved.value = true
                            showLeavingAlert.value = false
                            alertFinished()
                        }) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showLeavingAlert.value = false
                            alertFinished()
                        }) {
                            Text("No", color = Color.Red)
                        }
                    }
                )
            }

            var currentTabIndex by remember { mutableStateOf(0) }
            Editor(
                currentTabIndex = currentTabIndex,
                onCurrentTabIndexChange = {
                    currentTabIndex = it
                },
                topBarTitleRequest = {
                    topBarTitle = it
                },
                articleTitle = title,
                onArticleTitleChange = {
                    title = it
                    saved.value = false
                },
                articleContent = articleContent,
                onArticleContentChange = {
                    if (articleContent.text != it.text) {
                        saved.value = false
                    }
                    articleContent = it
                    isContentError = false
                },
                isContentError = isContentError
            )
        }
    }

    private fun save(
        editorViewModel: EditorViewModel,
        title: String,
        content: TextFieldValue,
        onSuccess: () -> Unit) {
        editorViewModel.save(title, content, onSuccess = onSuccess)
        saved.value = true
    }

    @Composable
    fun SendIcon() {
        val viewModel = viewModel<EditorViewModel>(factory = EditorViewModel.factory(LocalContext.current))
        if (viewModel.sendingState.value == EditorViewModel.SendState.Sending) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
        } else {
            Icon(Icons.Default.Send, "Send", tint = Color.White)
        }
    }

    //return whether content is empty
    private fun send(
        viewModel: EditorViewModel,
        title: String,
        content: String
    ): Boolean {
        return if (content.isNotEmpty()) {
            viewModel.send(title, content) {
                saved.value = true
                viewModel.save("", TextFieldValue())
                backToPreviousScreen()
            }
            false
        } else {
            true
        }
    }

    //return whether content is empty
    private fun edit(
        viewModel: EditorViewModel,
        title: String,
        content: String,
    ): Boolean {
        return if (content.isNotEmpty()) {
            viewModel.edit(editModeSetting!!.id, title, content) {
                saved.value = true
                backToPreviousScreen()
            }
            false
        } else {
            true
        }
    }

    private var leaving: (() -> Unit)? = null
    override val leavingScreen: (() -> Unit) -> Unit = {
        if (!saved.value && !inEditArticleMode) {
            showLeavingAlert.value = true
            leaving = it
        } else {
            it()
        }
    }

    private fun alertFinished() {
        saved.value = true
        leaving?.invoke()
        leaving = null
    }
}