package personal.secminhr.cavern.main.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(icons: @Composable RowScope.() -> Unit,
           title: MutableState<String>,
           showBackButton: Boolean = false,
           backAction: () -> Unit = {}) {
    TopAppBar {
        Row(modifier = Modifier.align(Alignment.CenterVertically).weight(1.0f)) {
            if(showBackButton) {
                IconButton(onClick = backAction) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
            Crossfade(targetState = title.value, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 16.dp)) {
                Text(it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        icons(this)
//        for (i in icons.indices) {
//            IconButton(onClick = { iconActions.getOrNull(i)?.invoke() }, modifier = Modifier.align(Alignment.CenterVertically)) {
//                icons[i]()
//            }
//        }
    }
}


val DefaultIcon = @Composable {
    Icon(Icons.Default.AccountCircle, null)
}

//@Preview(showBackground = true, name = "Not login")
//@Composable
//fun NoLoginBar() {
//    AppBar(listOf(DefaultIcon), title=mutableStateOf("Cavern"))
//}

//@Preview(showBackground = true, name = "logged in")
//@Composable
//fun LoginBar() {
//    AppBar(icons = listOf({
//        Image(
//            painterResource(id = R.drawable.user), null,
//            modifier = Modifier.clip(CircleShape).size(24.dp))
//    }), title= mutableStateOf("Cavern"))
//}