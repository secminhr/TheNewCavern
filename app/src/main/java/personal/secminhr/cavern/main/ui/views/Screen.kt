package personal.secminhr.cavern.main.ui.views

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import personal.secminhr.cavern.main.MainActivity.Companion.screenHistory

interface Screen {

    @Composable
    fun Content(showSnackbar: (String) -> Unit)
    val topBarIcons: @Composable RowScope.() -> Unit
    val topBarTitle: MutableState<String>
        get() = mutableStateOf("Cavern")
    val shouldShowBackButton: Boolean

    fun sameAppBarIconAs(screen: Screen): @Composable RowScope.() -> Unit = screen.topBarIcons

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