package personal.secminhr.cavern.ui.views.editor

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.ui.style.purple500
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.ui.views.markdown.MarkdownView
import personal.secminhr.cavern.viewmodel.EditorViewModel

class EditorScreen: Screen {
    private val saved = mutableStateOf(true)
    private val showLeavingAlert = mutableStateOf(false)
    private lateinit var editorViewModel: EditorViewModel
    private val isContentError = mutableStateOf(false)

    override val content = @Composable {
        editorViewModel = viewModel(factory = EditorViewModel.factory(LocalContext.current))
        val currentTabIndex = remember { mutableStateOf(0) }
        val title = remember { editorViewModel.getTitle() }
        val articleContent = remember { editorViewModel.getContent() }

        if (showLeavingAlert.value) {
            AlertDialog(onDismissRequest = {},
                        title = { Text("Saving?") },
                        text = { Text("Your draft hasn't been saved.\nDo you want to save it?") },
                        confirmButton = {
                            TextButton(onClick = {
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

    val saveIcon = @Composable {
        Icon(Icons.Default.Save, "Save")
    }

    val sendIcon = @Composable {
        val viewModel = viewModel<EditorViewModel>(factory = EditorViewModel.factory(LocalContext.current))
        if (viewModel.sendingState.value == EditorViewModel.SendState.Sending) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
        } else {
            Icon(Icons.Default.Send, "Send")
        }
    }

    override val topBarIcons = listOf(saveIcon, sendIcon)
    override val topBarIconActions = listOf(
        {
            editorViewModel.save()
            saved.value = true
        },
        {
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
    )
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