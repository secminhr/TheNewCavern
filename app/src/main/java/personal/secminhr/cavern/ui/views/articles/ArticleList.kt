package personal.secminhr.cavern.ui.views.articles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview


@Composable
fun ArticleList(list: List<ArticlePreview>, onLikeClicked: (Int) -> Unit = {}, onItemClicked: (ArticlePreview) -> Unit = {}, modifier: Modifier = Modifier) {

    var listState = rememberLazyListState()


    if(list.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        LazyColumnFor(items = list, state = listState, modifier = modifier) {
            listState = rememberLazyListState()
            ArticlePreviewItem(
                    preview = it,
                    onLikeClicked = onLikeClicked,
                    onItemClicked = onItemClicked
            )
            Divider()
        }
    }
}




//@Preview(showBackground = true, name = "List")
//@Composable
//fun List() {
//    ArticleList(
//        list = listOf(
//            articlePreviewNormalDislike, articlePreviewNormalLike
//        )
//    )
//}
//
//@Preview(showBackground = true, name = "Loading")
//@Composable
//fun LoadingPreview() {
//    ArticleList(list = listOf())
//}