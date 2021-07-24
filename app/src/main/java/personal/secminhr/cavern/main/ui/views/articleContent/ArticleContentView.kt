package personal.secminhr.cavern.main.ui.views.articleContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.ui.views.markdown.MarkdownView
import personal.secminhr.cavern.main.viewmodel.ArticleViewModel
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.Comment

@ExperimentalMaterialApi
@Composable
fun ArticleContentView(
    article: Article,
    titleState: MutableState<String>,
    comments: List<Comment>,
    onShowBottomRequest: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    modifier: Modifier,
) {
    var likeState by remember { mutableStateOf(article.isLiked) }
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier then Modifier.verticalScroll(scrollState)
    ) {

        if (scrollState.value > 0) {
            titleState.value = article.title
        } else {
            titleState.value = "Cavern"
        }

        val viewModel = viewModel<ArticleViewModel>()
        ArticleInfo(article, likeState, onAuthorClicked = {
            onShowBottomRequest(article.authorUsername)
        }, onLikeClicked = {
            viewModel.likeArticle(article.id) { success ->
                if (success) {
                    likeState = !likeState
                }
            }
        })

        if (article.content == "") {
            CircularProgressIndicator()
        } else {
            MarkdownView(article.content, onUserLinkClicked = onShowBottomRequest)
        }

        Text("留言")
        Divider(modifier = Modifier.padding(top = 14.dp, bottom = 14.dp))

        val currentUserViewModel = viewModel<CurrentUserViewModel>()
        comments.forEach {
            Comment(
                comment = it,
                onUserLinkClicked = onShowBottomRequest,
                showReplyButton = currentUserViewModel.currentUser != null,
                replyClicked = {
                    onReplyClicked(it.commenterUsername)
                }
            )
        }
        if (comments.isNullOrEmpty()) {
            Text("No comments", color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
fun ArticleInfo(article: Article, isLiked: Boolean, onAuthorClicked: () -> Unit, onLikeClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(article.title,
                modifier = Modifier.padding(start = 16.dp, top =  16.dp, end = 64.dp),
                style = MaterialTheme.typography.subtitle1)
            TextButton(
                onClick = onAuthorClicked,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(article.authorNickname, style = MaterialTheme.typography.button)
            }
        }

        IconButton(
            onClick = onLikeClicked,
            modifier = Modifier.align(Alignment.CenterEnd).size(48.dp)
        ) {
            if(isLiked) {
                Icon(Icons.Filled.ThumbUp, "Liked")
            } else {
                Icon(Icons.Outlined.ThumbUp, "")
            }
        }
    }
}