package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

interface Header: Tool {
    val sharps: String

    override fun transform(textFieldValue: TextFieldValue): TextFieldValue {
        val oldContent = textFieldValue.text
        val cursorPos = textFieldValue.selection.min

        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        var sharps = this.sharps
        val frontNewline = beforeCursorContent.isNotEmpty() && beforeCursorContent.last() != '\n'
        val endNewline = afterCursorContent.isNotEmpty() && afterCursorContent.first() != '\n'
        if(frontNewline) {
            sharps = "\n$sharps"
        }
        if(endNewline) {
            sharps += "\n"
        }
        val offset = sharps.trimEnd('\n').length
        return TextFieldValue(text = "$beforeCursorContent$sharps$afterCursorContent",
            selection = TextRange(beforeCursorContent.length + offset)
        )
    }
}