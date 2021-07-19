package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun Editor(text: State<TextFieldValue>, onTextChange: (TextFieldValue) -> Unit, isError: Boolean) {

    TextField(value = text.value, modifier = Modifier.fillMaxWidth().background(Color.White),
        placeholder = { Text("Content") },
        onValueChange = onTextChange,
        isError = isError
    )
}