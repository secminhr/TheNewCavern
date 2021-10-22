package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue

interface Tool {
    val icon: ImageVector
    val description: String
    fun transform(textFieldValue: TextFieldValue): TextFieldValue
}