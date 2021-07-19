package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Bold(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.FormatBold
    override val description = "Bold"
    override val action = {
        val styleSymbol = "****"
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        TextFieldValue(text = "$beforeCursorContent$styleSymbol$afterCursorContent",
                selection = TextRange(beforeCursorContent.length + styleSymbol.length / 2))
    }
}