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

    val leavingScreen: (() -> Unit) -> Unit
        get() = { it() }
}

fun Screen.sameAppBarIconAs(screen: Screen): @Composable RowScope.() -> Unit {
    return screen.topBarIcons
}

fun Screen.navigateTo(screen: Screen) {
    leavingScreen {
        screenHistory.changeScreen(screen)
    }
}

fun Screen.backToPreviousScreen() {
    leavingScreen {
        screenHistory.back()
    }
}