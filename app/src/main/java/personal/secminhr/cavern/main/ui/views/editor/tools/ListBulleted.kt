package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object ListBulleted: Tool {
    override val icon = Icons.Default.FormatListBulleted
    override val description = "Bulleted list"

    override fun transform(textFieldValue: TextFieldValue): TextFieldValue {
        val oldContent = textFieldValue.text
        val cursorPos = textFieldValue.selection.min

        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        var symbol = "- "
        val frontNewline = beforeCursorContent.isNotEmpty() && beforeCursorContent.last() != '\n'
        val endNewline = afterCursorContent.isNotEmpty() && afterCursorContent.first() != '\n'
        if(frontNewline) {
            symbol = "\n$symbol"
        }
        if(endNewline) {
            symbol += "\n"
        }
        val offset = symbol.trimEnd('\n').length

        return TextFieldValue(text = "$beforeCursorContent$symbol$afterCursorContent",
            TextRange(beforeCursorContent.length + offset)
        )
    }
}