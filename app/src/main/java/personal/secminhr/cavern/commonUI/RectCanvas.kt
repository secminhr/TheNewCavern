package personal.secminhr.cavern.commonUI

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.dp

@Composable
fun RectCanvas(modifier: Modifier, onDraw: DrawScope.()->Unit) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val horizontalInset = (size.width-size.minDimension) / 2f
        val verticalInset = (size.height-size.minDimension) / 2f
        inset(horizontalInset, verticalInset, onDraw)
    }
}

val DrawScope.length: Float
    get() = size.minDimension
val DrawScope.scale: Float
    get() = length / 435.dp.toPx() //435 is the original size of the icon