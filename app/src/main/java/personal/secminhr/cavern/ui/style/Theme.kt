package personal.secminhr.cavern.ui.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val DarkColorPalette = darkColors(
        primary = purple200,
        primaryVariant = purple700,
        secondary = teal200
)

private val LightColorPalette = lightColors(
        primary = lightPrimary,
        primaryVariant = lightPrimaryDark,
        secondary = lightSecondary

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CavernTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if(darkTheme) DarkColorPalette else LightColorPalette
    //val colors = LightColorPalette
    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes
    ) {
        Surface {
            content()
        }
    }
}