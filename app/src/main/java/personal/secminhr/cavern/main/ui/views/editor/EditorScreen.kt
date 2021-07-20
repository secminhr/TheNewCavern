package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.ui.style.purple500
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.ui.views.markdown.MarkdownView
import personal.secminhr.cavern.main.viewmodel.EditorViewModel

class EditorScreen(val useTitle: String? = null, val useContent: String? = null, val id: Int? = null): Screen {
    private val saved = mutableStateOf(true)
    private val showLeavingAlert = mutableStateOf(false)
    private lateinit var editorViewModel: EditorViewModel
    private val isContentError = mutableStateOf(false)
    private val inEditArticleMode = (useTitle != null)
    lateinit var title: MutableState<String>
    lateinit var articleContent: MutableState<TextFieldValue>

    @Composable
    override fun Content(showSnackbar: (String) -> Unit) {
        editorViewModel = viewModel(factory = EditorViewModel.factory(LocalContext.current))
        val currentTabIndex = remember { mutableStateOf(0) }
        title = remember { if (!inEditArticleMode) editorViewModel.getTitle() else mutableStateOf(useTitle!!) }
        articleContent = remember{ if (!inEditArticleMode) editorViewModel.getContent() else mutableStateOf(TextFieldValue(text=useContent!!)) }

        if (showLeavingAlert.value && !inEditArticleMode) {
            AlertDialog(onDismissRequest = {},
                title = { Text("Saving?") },
                text = { Text("Your draft hasn't been saved.\nDo you want to save it?") },
                confirmButton = {
                    Button(onClick = {
                        editorViewModel.save()
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

        Column {
            TabRow(selectedTabIndex = currentTabIndex.value,
                backgroundColor = purple500,
                contentColor = Color.White) {
                Tab(selected = currentTabIndex.value == 0,
                    text = { Text("Editor") },
                    onClick = { currentTabIndex.value = 0 }
                )
                val manager = LocalFocusManager.current
                Tab(selected = currentTabIndex.value == 1,
                    text = { Text("Preview") },
                    onClick = {
                        manager.clearFocus()
                        currentTabIndex.value = 1
                    }
                )
            }
            if (currentTabIndex.value == 0) {
                topBarTitle.value = "Cavern"
                Title(title) {
                    title.value = it
                    saved.value = false
                }
                Tools(articleContent) {
                    articleContent.value = it
                    saved.value = false
                }
                Editor(articleContent, isError = isContentError.value, onTextChange = {
                    if (articleContent.value.text != it.text) {
                        saved.value = false
                    }
                    articleContent.value = it
                    isContentError.value = false
                })
            } else {
                topBarTitle.value = title.value
                MarkdownView(text = articleContent.value.text)
            }
        }
    }

    private fun save() {
        editorViewModel.save()
        saved.value = true
    }

    @Composable
    fun SendIcon() {
        val viewModel = viewModel<EditorViewModel>(factory = EditorViewModel.factory(LocalContext.current))
        if (viewModel.sendingState.value == EditorViewModel.SendState.Sending) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
        } else {
            Icon(Icons.Default.Send, "Send")
        }
    }

    private fun send() {
        editorViewModel.save()
        if (editorViewModel.getContent().value.text.isNotEmpty()) {
            editorViewModel.send {
                saved.value = true
                backToPreviousScreen()
            }
            isContentError.value = false
        } else {
            isContentError.value = true
        }
    }

    private fun edit() {
        if (articleContent.value.text.isNotEmpty()) {
            editorViewModel.edit(id!!, title.value, articleContent.value.text) {
                saved.value = true
                backToPreviousScreen()
            }
            isContentError.value = false
        } else {
            isContentError.value = true
        }
    }

    override val topBarIcons: @Composable RowScope.() -> Unit = {
        if (!inEditArticleMode) {
            IconButton(onClick = { save() }) {
                Icon(Icons.Default.Save, "Save")
            }
        }
        IconButton(onClick = {
            if (!inEditArticleMode) {
                send()
            } else {
                edit()
            }
        }) {
            SendIcon()
        }
    }

    override val shouldShowBackButton = true
    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")

    private var leaving: (() -> Unit)? = null
    override val leavingScreen: (() -> Unit) -> Unit = {
        if (!saved.value) {
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