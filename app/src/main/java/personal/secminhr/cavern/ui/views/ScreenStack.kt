package personal.secminhr.cavern.ui.views

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

class ScreenStack(initScreen: Screen) {
    private val history = Stack<Screen>()
    val currentScreen: MutableState<Screen> = mutableStateOf(initScreen)

    init {
        history.push(initScreen)
    }

    fun changeScreen(screen: Screen) {
        history.push(screen)
        currentScreen.value = screen
    }

    fun back() {
        history.pop()
        currentScreen.value = history.peek()
    }
}