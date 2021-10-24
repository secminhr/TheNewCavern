package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import personal.secminhr.cavern.main.ui.style.lightPrimaryDark

@Composable
fun Title(title: String, onTitleChange: (String) -> Unit) {
    TextField(value = title, modifier = Modifier.fillMaxWidth().background(lightPrimaryDark),
        placeholder = { Text("Title", color = Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        onValueChange = onTitleChange,
        shape = RectangleShape
    )
}