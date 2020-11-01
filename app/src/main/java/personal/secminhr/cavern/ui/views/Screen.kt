package personal.secminhr.cavern.ui.views

import androidx.compose.runtime.Composable

interface Screen {
    val content: @Composable() () -> Unit
    val topBarIcon: @Composable() () -> Unit
    val topBarIconAction: (ScreenStack) -> Unit

    companion object EmptyScreen: Screen {

        override val content = @Composable {
            //empty
        }

        override val topBarIcon = @Composable {
            //empty
        }

        override val topBarIconAction = { _: ScreenStack ->
            //empty
        }
    }
}