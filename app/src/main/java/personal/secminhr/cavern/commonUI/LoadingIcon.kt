package personal.secminhr.cavern.commonUI

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import personal.secminhr.cavern.R
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

private fun DrawScope.yFull() = Offset(0f, length)
private fun DrawScope.xFull() = Offset(length, 0f)
private fun DrawScope.yHalf() = yFull() / 2f
private fun DrawScope.xHalf() = xFull() / 2f
private fun DrawScope.center() = xHalf() + yHalf()


private fun DrawScope.greenCross() {
    val strokeWidth = 8.dp.toPx() * scale
    inset(length / 9) {
        for (deg in 0..90 step 90) {
            rotate(deg.toFloat()) {
                drawLine(
                    color = logo_green,
                    start = yHalf(),
                    end = yHalf() + xFull(),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun DrawScope.orangeRect(degree: Float) {
    val strokeWidth = 8.dp.toPx() * scale
    rotate(degree) {
        //rectSize/length
        val rectSizeToLengthRatio = 0.11f

        val rectSize = rectSizeToLengthRatio * length
        drawRect(
            color = logo_orange,
            topLeft = center() - Offset(rectSize, rectSize)/2f,
            size = Size(rectSize, rectSize),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

private fun DrawScope.blueRect(ratio: Float) {
    val start = center() - xHalf()/2f
    val end = center() - yHalf()/2f
    val outerEnd = lerp(start, end, ratio)
    val innerYTransition = 30.dp.toPx() * scale
    val innerStart = outerEnd + Offset(1f, -1f) * innerYTransition / 2f

    val strokeWidth = 15.dp.toPx() * scale
    for (degree in 0..270 step 90) {
        rotate(degree.toFloat()) {
            clipRect(
                left = 0f,
                top = 0f,
                right = length / 2f,
                bottom = length / 2f,
                clipOp = ClipOp.Intersect
            ) {
                drawLine(
                    color = logo_light_blue,
                    start = start,
                    //using plot slope form to construct the end point, and assume that abs(slope) == 1
                    end = outerEnd,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                translate(top = innerYTransition) {
                    drawLine(
                        color = logo_light_blue,
                        start = innerStart,
                        end = end,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Preview(name = "Original size 435", widthDp = 435, heightDp = 435, showBackground = true)
@Composable
fun LoadingIcon_Preview() {
    LoadingIcon()
}

@Preview(name = "dp-sized 435", widthDp = 435, heightDp = 435, showBackground = true)
@Composable
fun LoadingIcon_PreviewExprDpSized() {
    LoadingIcon()
}

@Preview(name = "original img", heightDp = 435, widthDp = 435, showBackground = true)
@Composable
fun OriginalImg_Preview() {
    val painter = painterResource(id = R.drawable.logo_img)
    Image(painter = painter, contentDescription = "", modifier = Modifier.padding(43.5.dp))
}
//
//@Preview(name = "Height 200", heightDp = 200, showBackground = true)
//@Composable
//fun LoadingIcon_PreviewH100() {
//    LoadingIcon()
//}