package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Photo(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.Photo
    override val description = "Photo"

    override val action = {
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        TextFieldValue(text = "$beforeCursorContent![]()$afterCursorContent",
                TextRange(beforeCursorContent.length + 2)
        )
    }
}