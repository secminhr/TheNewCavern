package personal.secminhr.cavern.main.ui.views.editor

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import personal.secminhr.cavern.main.ui.views.editor.tools.*

@Composable
fun Tools(text: TextFieldValue, onModify: (TextFieldValue) -> Unit) {

    val tools = listOf(
        Header1,
        Header2,
        Header3,
        Header4,
        Header5,
        Header6,
        Link,
        Italic,
        Bold,
        Strikethrough,
        Photo,
        ListBulleted,
        ListNumbered,
        Quote,
        Code
    )

    LazyRow {
        items(items = tools) {
            IconButton(onClick = {
                onModify(it.transform(text))
            }) {
                Icon(it.icon, it.description)
            }
        }
    }
}