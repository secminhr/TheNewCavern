package personal.secminhr.cavern.main.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(bar: BarScope, showBackButton: Boolean, backAction: () -> Unit) {
    TopAppBar {
        if (showBackButton) {
            IconButton(onClick = backAction) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
            }
        }

        Crossfade(
            targetState = bar.title,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
                .weight(1.0f),
        ) {
            Text(
                it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterVertically),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
        for ((icon, action) in bar.iconButtons) {
            IconButton(onClick = action) {
                icon()
            }
        }
    }
}

//@Composable
//fun AppBar(icons: @Composable RowScope.() -> Unit,
//           title: String,
//           showBackButton: Boolean = false,
//           backAction: () -> Unit = {}) {
//    TopAppBar {
//        Row(modifier = Modifier
//            .align(Alignment.CenterVertically)
//            .weight(1.0f)) {
//            if(showBackButton) {
//                IconButton(onClick = backAction) {
//                    Icon(Icons.Default.ArrowBack, "Back")
//                }
//            }
//            Crossfade(
//                targetState = title,
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .padding(start = 16.dp)
//            ) {
//
//                Text(it,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//        icons(this)
//    }
//}


//val DefaultIcon = @Composable {
//    Icon(Icons.Default.AccountCircle, null)
//}

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