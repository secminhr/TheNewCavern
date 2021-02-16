package personal.secminhr.cavern.ui.views.editor

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue
import personal.secminhr.cavern.ui.views.editor.tools.*

@Composable
fun Tools(text: State<TextFieldValue>, onModify: (TextFieldValue) -> Unit) {

    val tools = listOf(
        Header1(text),
        Header2(text),
        Header3(text),
        Header4(text),
        Header5(text),
        Header6(text),
        Link(text),
        Italic(text),
        Bold(text),
        Strikethrough(text),
        Photo(text),
        ListBulleted(text),
        ListNumbered(text),
        Quote(text),
        Code(text)
    )

    LazyRow {
        items(items = tools) {
            IconButton(onClick = {
                onModify(it.action())
            }) {
                Icon(it.icon, it.description)
            }
        }
    }
}