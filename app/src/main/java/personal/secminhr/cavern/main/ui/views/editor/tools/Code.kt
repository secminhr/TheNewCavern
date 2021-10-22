package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object Code : Tool {
    override val icon = Icons.Default.Code
    override val description = "Code"
    override fun transform(textFieldValue: TextFieldValue): TextFieldValue {
        val oldContent = textFieldValue.text
        val cursorPos = textFieldValue.selection.min

        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)

        val frontNewline = beforeCursorContent.isNotEmpty() && beforeCursorContent.last() != '\n'
        val endNewline = afterCursorContent.isNotEmpty() && afterCursorContent.first() != '\n'
        var symbol = "```\n\n```"
        if(frontNewline) {
            symbol = "\n$symbol"
        }
        if(endNewline) {
            symbol += "\n"
        }
        val offset = symbol.trimEnd('\n').length - 4

        return TextFieldValue(text = "$beforeCursorContent$symbol$afterCursorContent",
            TextRange(beforeCursorContent.length + offset)
        )
    }
}