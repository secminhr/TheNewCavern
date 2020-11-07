package personal.secminhr.cavern.ui.views.articleContent

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import personal.secminhr.cavern.ui.views.MarkdownView
import stoneapp.secminhr.cavern.cavernObject.Article

@Composable
fun ArticleContentView(article: State<Article>) {
    ScrollableColumn {
        ArticleInfo(article.value)

        if (article.value.content == "") {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
        } else {
            MarkdownView(text = article.value.content)
        }
    }
}

@Composable
fun ArticleInfo(article: Article) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(article.title, modifier = Modifier.padding(start = 16.dp, top =  16.dp, end = 64.dp),
                    style = MaterialTheme.typography.subtitle1)
            TextButton(onClick = {}, modifier = Modifier.padding(start = 8.dp)) {
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