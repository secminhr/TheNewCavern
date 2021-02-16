package personal.secminhr.cavern.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Code(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.Code
    override val description = "Code"
    override val action = {
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

        TextFieldValue(text = "$beforeCursorContent$symbol$afterCursorContent",
                TextRange(beforeCursorContent.length + offset)
        )
    }
}