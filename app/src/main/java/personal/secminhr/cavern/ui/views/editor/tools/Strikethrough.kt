package personal.secminhr.cavern.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StrikethroughS
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Strikethrough(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.StrikethroughS
    override val description = "Strikethrough"
    override val action = {
        val styleSymbol = "~~~~"
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        TextFieldValue(text = "$beforeCursorContent$styleSymbol$afterCursorContent",
                TextRange(beforeCursorContent.length + styleSymbol.length / 2)
        )
    }
}