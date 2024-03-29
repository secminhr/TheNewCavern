package personal.secminhr.cavern.main.ui.views.articles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ArticlePreviewItem(
    preview: ArticlePreview,
    onItemClicked: (ArticlePreview) -> Unit = {},
    onLikeClicked: (Int) -> Unit = {}
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { onItemClicked(preview) }, role = Role.Button)
    ) {
        InfoColumn(
            preview = preview,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        LikeData(
            preview = preview,
            modifier = Modifier.align(Alignment.CenterEnd),
            onLikeClicked = onLikeClicked
        )
    }
}

@Composable
fun InfoColumn(preview: ArticlePreview, modifier: Modifier) {
    Column(
        modifier = modifier then
                    Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 80.dp)
    ) {
        Text(preview.title, style = MaterialTheme.typography.subtitle1)
        Text(preview.author, style = MaterialTheme.typography.subtitle2)
        Text(preview.date.toStringWithFormat("yyyy/MM/dd"),
                fontSize = 12.sp,
                color = Color.Gray)
    }
}

@Composable
fun LikeData(preview: ArticlePreview,
             modifier: Modifier,
             onLikeClicked: (Int) -> Unit = {}) {
    Row(modifier = modifier then Modifier.padding(end = 16.dp)) {
        IconToggleButton(
                modifier = Modifier.width(48.dp).align(Alignment.CenterVertically),
                checked = preview.liked,
                onCheckedChange = {
                    onLikeClicked(preview.id)
                }
        ) {
            if (preview.liked) {
                Icon(Icons.Filled.ThumbUp, "Liked")
            } else {
                Icon(Icons.Outlined.ThumbUp, "")
            }
        }
        Text(preview.upvote,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterVertically))
    }
}


//@Preview(showBackground = true, name = "NormalItem")
//@Composable
//fun NormalItemPreview() {
//    ArticlePreviewItem(
//        articlePreviewNormalDislike
//    )
//}
//
//@Preview(showBackground = true, name = "NormalItemLiked")
//@Composable
//fun NormalItemLikedPreview() {
//    ArticlePreviewItem(
//        articlePreviewNormalLike
//    )
//}
//
//@Preview(showBackground = true, name = "LongTextItem")
//@Composable
//fun LongTextItemPreview() {
//    ArticlePreviewItem(
//        articlePreviewLongText
//    )
//}

fun Date.toStringWithFormat(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.US)
    return dateFormat.format(this)
}