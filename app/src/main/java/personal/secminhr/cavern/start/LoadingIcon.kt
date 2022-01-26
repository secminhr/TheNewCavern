package personal.secminhr.cavern.start

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import personal.secminhr.cavern.main.ui.style.logo_green
import personal.secminhr.cavern.main.ui.style.logo_light_blue
import personal.secminhr.cavern.main.ui.style.logo_orange

@Composable
fun LoadingIcon() {
    val rotateTransition = rememberInfiniteTransition()
    val rotate by rotateTransition.animateFloat(
        initialValue = 45f,
        targetValue = 405f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8*700
                val transitionDuration = 60

                45f at 0
                for (i in 1..8) {
                    val deg = 45f*i
                    deg at (700*i - transitionDuration) //stay
                    (deg+45f) at 700*i //change quickly to approximate spring
                }
            },
            repeatMode = RepeatMode.Restart
        )
    )

    val ratioTransition = rememberInfiniteTransition()
    val ratio by ratioTransition.animateFloat(
        initialValue = 0.55f ,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5600
                0.55f at 0
                1f at 2700 with FastOutSlowInEasing
                0f at 4200 with FastOutSlowInEasing
                0.55f at 5600 with LinearOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        )
    )

    RectCanvas(modifier = Modifier.zIndex(5f)) {
        orangeRect(rotate)
        blueRect(ratio)
        greenCross()
    }
}

@Composable
private fun RectCanvas(modifier: Modifier, onDraw: DrawScope.()->Unit) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val horizontalInset = (size.width-size.minDimension) / 2f
        val verticalInset = (size.height-size.minDimension) / 2f
        inset(horizontalInset, verticalInset, onDraw)
    }
}

private fun DrawScope.greenCross() {
    inset(size.width / 9) {
        for (deg in 0..90 step 90) {
            rotate(deg.toFloat()) {
                drawLine(
                    color = logo_green,
                    start = Offset(x = 0f, y = size.height / 2f),
                    end = Offset(x = size.width, y = size.height / 2f),
                    strokeWidth = 19.7f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun DrawScope.orangeRect(degree: Float) {
    rotate(degree) {
        val rectSizeRatioToWidth = 8.8f
        drawRect(
            color = logo_orange,
            topLeft = Offset(
                x = size.width / 2 - size.width / (2 * rectSizeRatioToWidth),
                y = size.height / 2 - size.width / (2 * rectSizeRatioToWidth)
            ),
            size = Size(size.width / rectSizeRatioToWidth, size.width / rectSizeRatioToWidth),
            style = Stroke(
                width = 19.7f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

private fun DrawScope.blueRect(ratio: Float) {
    val start = Offset(size.width / 4f, size.height / 2f)
    val startToMidX = size.width / 2f - size.width / 4f
    val outerEnd = start + Offset(startToMidX, -startToMidX) * ratio
    val innerYTransition = 78f
    val innerStart = outerEnd + Offset(1f, -1f) * innerYTransition / 2f
    val end = Offset(size.width / 2f, size.height / 4f)

    for (degree in 0..270 step 90) {
        rotate(degree.toFloat()) {
            clipRect(
                left = 0f,
                top = 0f,
                right = size.width / 2f,
                bottom = size.height / 2f,
                clipOp = ClipOp.Intersect
            ) {
                drawLine(
                    color = logo_light_blue,
                    start = start,
                    //using plot slope form to construct the end point, and assume that abs(slope) == 1
                    end = outerEnd,
                    strokeWidth = 36f,
                    cap = StrokeCap.Round
                )
                translate(top = 78f) {
                    drawLine(
                        color = logo_light_blue,
                        start = innerStart,
                        end = end,
                        strokeWidth = 36f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingIcon_Preview() {
    LoadingIcon()
}