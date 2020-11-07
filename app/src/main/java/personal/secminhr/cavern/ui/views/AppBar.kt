package personal.secminhr.cavern.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import personal.secminhr.cavern.R

@Composable
fun AppBar(icon: @Composable () -> Unit,
           title: MutableState<String>,
           showBackButton: Boolean = false,
           backAction: () -> Unit = {},
           iconAction: () -> Unit = {}) {
    TopAppBar {
        Row(modifier = Modifier.align(Alignment.CenterVertically).weight(1.0f)) {
            if(showBackButton) {
                IconButton(onClick = backAction) {
                    Icon(Icons.Default.ArrowBack)
                }
            }
            Crossfade(current = title.value, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 16.dp)) {
                Text(it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(onClick = iconAction, modifier = Modifier.align(Alignment.CenterVertically)) {
            icon()
        }
    }
}


val DefaultIcon = @Composable {
    Icon(Icons.Default.AccountCircle)
}

@Preview(showBackground = true, name = "Not login")
@Composable
fun NoLoginBar() {
    AppBar(DefaultIcon, title=mutableStateOf("Cavern"))
}

@Preview(showBackground = true, name = "logged in")
@Composable
fun LoginBar() {
    AppBar(icon = {
        Image(imageResource(id = R.drawable.user),
            modifier = Modifier.clip(CircleShape).size(24.dp))
    }, title= mutableStateOf("Cavern"))
}