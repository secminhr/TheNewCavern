package personal.secminhr.cavern.ui.views.articleContent

import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.ui.views.login.UserView
import personal.secminhr.cavern.ui.views.markdown.MarkdownView
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernObject.Comment

@ExperimentalMaterialApi
@Composable
fun ArticleContentView(article: State<Article>, preview: ArticlePreview, titleState: MutableState<String>, comments: State<List<Comment>>) {
    val bottomSheetScaffoldState =
            rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberBottomSheetState(
                            initialValue = BottomSheetValue.Collapsed
                    )
            )

    val bottomSheetUsername = mutableStateOf(preview.authorUsername)
    BottomSheetScaffold(sheetContent = {
        UserView(bottomSheetUsername)
    }, scaffoldState = bottomSheetScaffoldState, sheetPeekHeight = 0.dp) {
        val state = rememberScrollState()
        ScrollableColumn(scrollState = state, horizontalAlignment = Alignment.CenterHorizontally) {

            if (state.value > 0) {
                titleState.value = preview.title
            } else {
                titleState.value = "Cavern"
            }

            ArticleInfo(article.value) {
                bottomSheetUsername.value = article.value.authorUsername
                bottomSheetScaffoldState.bottomSheetState.expand()
            }

            if (article.value.content == "") {
                CircularProgressIndicator()
            } else {
                MarkdownView(article.value.content)
            }

            Text("留言")
            Divider(modifier = Modifier.padding(top = 14.dp, bottom = 14.dp))

            val comment = remember { mutableStateOf("") }
            comments.value.forEach {
                Comment(comment = it, onUserLinkClicked = {
                    bottomSheetUsername.value = it
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }, replyClicked = {
                    comment.value += "@${it.commenterUsername} "
                })
            }

            if (MainActivity.hasCurrentUser) {
                Divider()
                val scrollScope = rememberCoroutineScope()
                val interactionState = remember { InteractionState() }
                OutlinedTextField(value = comment.value,
                                    onValueChange = {
                                        comment.value = it
                                        scrollScope.launch {
                                            state.smoothScrollTo(state.maxValue)
                                        }
                                    },
                                    onTextInputStarted = {
                                        it.showSoftwareKeyboard()
                                        scrollScope.launch {
                                            state.smoothScrollTo(state.maxValue)
                                        }
                                    },
                                    label = { Text("Your new comment") },
                                     modifier = Modifier.padding(16.dp),
                                    interactionState = interactionState
                )
            }


        }
    }
}

@Composable
fun ArticleInfo(article: Article, onAuthorClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(article.title, modifier = Modifier.padding(start = 16.dp, top =  16.dp, end = 64.dp),
                style = MaterialTheme.typography.subtitle1)
            TextButton(onClick = onAuthorClicked, modifier = Modifier.padding(start = 8.dp)) {
                Text(article.authorNickname, style = MaterialTheme.typography.button)
            }
        }

        IconButton(onClick = {}, modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(48.dp)) {
            if(article.isLiked) {
                Icon(Icons.Filled.ThumbUp, "Liked")
            } else {
                Icon(Icons.Outlined.ThumbUp, "")
            }
        }
    }
}