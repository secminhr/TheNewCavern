package personal.secminhr.cavern.ui.views.articleContent

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import personal.secminhr.cavern.ui.views.MarkdownView
import personal.secminhr.cavern.ui.views.login.UserView
import personal.secminhr.cavern.viewmodel.AuthorInfoViewModel
import stoneapp.secminhr.cavern.api.results.Author
import stoneapp.secminhr.cavern.cavernObject.Article

@ExperimentalMaterialApi
@Composable
fun ArticleContentView(article: State<Article>, titleState: MutableState<String>) {
    val scrollState = rememberScrollState()
    val bottomSheetScaffoldState =
            rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberBottomSheetState(
                            initialValue = BottomSheetValue.Collapsed
                    )
            )
    val author: MutableState<Author?> = mutableStateOf(null)
    BottomSheetScaffold(sheetContent = {
        if (author.value == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Column {
                IconButton(onClick = { bottomSheetScaffoldState.bottomSheetState.collapse() }) {
                    Icon(Icons.Default.Close)
                }
                UserView(author.value!!.account)
            }
        }
    }, scaffoldState = bottomSheetScaffoldState, sheetPeekHeight = 0.dp) {
        ScrollableColumn(scrollState = scrollState) {
            if (scrollState.value > 0) {
                titleState.value = article.value.title
            } else {
                titleState.value = "Cavern"
            }
            ArticleInfo(article.value, author) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }

            if (article.value.content == "") {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else {
                MarkdownView(text = article.value.content)
            }
        }
    }
}

@Composable
fun ArticleInfo(article: Article, authorInfo: MutableState<Author?>, onAuthorClicked: () -> Unit) {

    viewModel<AuthorInfoViewModel>().getAuthorInfo(article.authorUsername) {
        authorInfo.value = it
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(article.title, modifier = Modifier.padding(start = 16.dp, top =  16.dp, end = 64.dp),
                    style = MaterialTheme.typography.subtitle1)
            TextButton(onClick = onAuthorClicked, modifier = Modifier.padding(start = 8.dp)) {
                Text(article.authorNickname, style = MaterialTheme.typography.button)
            }
        }
        IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd).size(48.dp)) {
            if(article.isLiked) {
                Icon(Icons.Filled.ThumbUp)
            } else {
                Icon(Icons.Outlined.ThumbUp)
            }
        }
    }
}

//@Preview(showBackground = true, name = "Normal")
//@Composable
//fun ArticleContentViewPreview() {
//    ArticleContentView(articleContentDislike.value!!)
//}