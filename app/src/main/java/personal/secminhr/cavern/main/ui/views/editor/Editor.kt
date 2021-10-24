package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import personal.secminhr.cavern.main.ui.views.markdown.MarkdownView

@Composable
fun Editor(
    currentTabIndex: Int,
    onCurrentTabIndexChange: (Int) -> Unit,
    topBarTitleRequest: (String) -> Unit,
    articleTitle: String,
    onArticleTitleChange: (String) -> Unit,
    articleContent: TextFieldValue,
    onArticleContentChange: (TextFieldValue) -> Unit,
    isContentError: Boolean
) {
    Column {
        TabRow(
            selectedTabIndex = currentTabIndex,
            contentColor = Color.White
        ) {
            Tab(selected = currentTabIndex == 0,
                text = { Text("Editor") },
                onClick = { onCurrentTabIndexChange(0) }
            )
            val manager = LocalFocusManager.current
            Tab(selected = currentTabIndex == 1,
                text = { Text("Preview") },
                onClick = {
                    manager.clearFocus()
                    onCurrentTabIndexChange(1)
                }
            )
        }

        if (currentTabIndex == 0) {
            topBarTitleRequest("Cavern")
            Title(articleTitle, onTitleChange = onArticleTitleChange)
            Tools(articleContent, onModify = onArticleContentChange)
            Divider()
            EditArea(
                articleContent,
                isError = isContentError,
                onTextChange = onArticleContentChange)
        } else {
            topBarTitleRequest(articleTitle)
            MarkdownView(text = articleContent.text)
        }
    }
}

@Composable
fun EditArea(text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit, isError: Boolean) {

    TextField(value = text, modifier = Modifier.fillMaxSize(),
        placeholder = { Text("Content") },
        onValueChange = onTextChange,
        isError = isError,
        colors = textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent
        )
    )
}