package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object Photo: Tool {
    override val icon = Icons.Default.Photo
    override val description = "Photo"

    override fun transform(textFieldValue: TextFieldValue): TextFieldValue {
        val oldContent = textFieldValue.text
        val cursorPos = textFieldValue.selection.min

        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        return TextFieldValue(text = "$beforeCursorContent![]()$afterCursorContent",
            TextRange(beforeCursorContent.length + 2)
        )
    }
}