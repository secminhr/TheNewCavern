package personal.secminhr.cavern.main.ui.views

import androidx.compose.runtime.*
import personal.secminhr.cavern.main.MainActivity.Companion.screenHistory

typealias ComposableFunc =  @Composable () -> Unit
typealias ClickFunc = () -> Unit

abstract class Screen {

    @Composable
    abstract fun Screen(showSnackbar: (String) -> Unit)

    var shouldShowBackButton: Boolean = false
        private set
    open val leavingScreen: (() -> Unit) -> Unit
        get() = { it() }

    var barScope by mutableStateOf(BarScope())
        private set
    fun appBar(showBackButton: Boolean = false, bar: BarScope.() -> Unit) {
        barScope = BarScope()
        shouldShowBackButton = showBackButton
        bar(barScope)
    }

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


@DslMarker
annotation class CavernDsl

@CavernDsl
class BarScope {
    val iconButtons = mutableStateListOf<Pair<ComposableFunc, ClickFunc>>()
    var  title: String by mutableStateOf("Cavern")
        private set

    fun title(title: String) {
        this.title = title
    }

    fun iconButton(action: () -> Unit, icon: @Composable () -> Unit) {
        iconButtons.add(icon to action)
    }

    fun iconsFrom(screen: Screen) {
        iconButtons.addAll(screen.barScope.iconButtons)
    }
}