package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object Italic: Tool {
    override val icon = Icons.Default.FormatItalic
    override val description = "Italic"

    override fun transform(textFieldValue: TextFieldValue): TextFieldValue {
        val oldContent = textFieldValue.text
        val cursorPos = textFieldValue.selection.min

        val styleSymbol = "**"
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        return TextFieldValue(text = "$beforeCursorContent$styleSymbol$afterCursorContent",
            TextRange(beforeCursorContent.length + styleSymbol.length / 2)
        )
    }
}