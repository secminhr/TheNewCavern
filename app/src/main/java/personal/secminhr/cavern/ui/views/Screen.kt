package personal.secminhr.cavern.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import personal.secminhr.cavern.MainActivity.Companion.screenHistory

interface Screen {
    val content: @Composable () -> Unit
    val topBarIcons: List<@Composable () -> Unit>
    val topBarIconActions: List<() -> Unit>
    val topBarTitle: MutableState<String>
        get() = mutableStateOf("Cavern")
    val shouldShowBackButton: Boolean

    fun sameAppBarIconAs(screen: Screen): List<@Composable () -> Unit> = screen.topBarIcons
    fun sameAppBarIconActionAs(screen: Screen) = screen.topBarIconActions

    val leavingScreen: (() -> Unit) -> Unit
        get() = { it() }

    fun navigateTo(screen: Screen) {
        leavingScreen {
            screenHistory.changeScreen(screen)
        }
    }

    fun backToPreviousScreen() {
        leavingScreen {
            screenHistory.back()
        }
    }
}