package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Italic(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.FormatItalic
    override val description = "Italic"
    override val action = {
        val styleSymbol = "**"
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        TextFieldValue(text = "$beforeCursorContent$styleSymbol$afterCursorContent",
                TextRange(beforeCursorContent.length + styleSymbol.length / 2)
        )
    }
}