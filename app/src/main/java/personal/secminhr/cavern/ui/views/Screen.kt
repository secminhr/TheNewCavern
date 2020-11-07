package personal.secminhr.cavern.ui.views

import androidx.compose.runtime.Composable
import personal.secminhr.cavern.MainActivity.Companion.screenHistory

interface Screen {
    val content: @Composable () -> Unit
    val topBarIcon: @Composable () -> Unit
    val topBarIconAction: () -> Unit
    val shouldShowBackButton: Boolean

    fun sameAppBarIconAs(screen: Screen): @Composable () -> Unit = screen.topBarIcon
    fun sameAppBarIconActionAs(screen: Screen) = screen.topBarIconAction

    fun navigateTo(screen: Screen) {
        screenHistory.changeScreen(screen)
    }

    fun backToPreviousScreen() {
        screenHistory.back()
    }

    companion object EmptyScreen: Screen {

        override val content = @Composable {
            //empty
        }

        override val topBarIcon = @Composable {
            //empty
        }

        override val topBarIconAction = {
            //empty
        }
        override val shouldShowBackButton = false
    }
}