package personal.secminhr.cavern.ui.views.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import personal.secminhr.cavern.ui.style.purple700

@Composable
fun Title(title: State<String>, onTitleChange: (String) -> Unit) {
    TextField(value = title.value, modifier = Modifier.fillMaxWidth().background(purple700),
        placeholder = { Text("Title", color = Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        onValueChange = onTitleChange,
        shape = RectangleShape
    )
}