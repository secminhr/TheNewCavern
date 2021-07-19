package personal.secminhr.cavern.main.ui.views.articleContent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import personal.secminhr.cavern.main.MainActivity
import personal.secminhr.cavern.main.ui.views.markdown.MarkdownView
import stoneapp.secminhr.cavern.cavernObject.Comment

@Composable
fun Comment(comment: Comment, onUserLinkClicked: (String) -> Unit, replyClicked: () -> Unit) {

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = comment.commenterNickname, modifier = Modifier.padding(start = 16.dp))
            if (MainActivity.hasCurrentUser) {
                IconButton(onClick = replyClicked) {
                    Icon(Icons.Default.Reply, "Reply")
                }
            }
        }
        MarkdownView(text = comment.content, modifier = Modifier.padding(start=56.dp, end = 16.dp),
                    onUserLinkClicked = onUserLinkClicked)
    }
}