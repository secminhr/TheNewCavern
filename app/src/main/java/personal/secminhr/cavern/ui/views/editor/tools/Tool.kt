package personal.secminhr.cavern.ui.views.editor.tools

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue

interface Tool {
    val text: State<TextFieldValue>
    val icon: ImageVector
    val description: String
    val action: () -> TextFieldValue

    val oldContent get() = text.value.text
    val cursorPos get() = text.value.selection.min
}