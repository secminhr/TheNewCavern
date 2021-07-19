package personal.secminhr.cavern.main.ui.views

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

class ScreenStack() {
    private val history = Stack<Screen>()
    val currentScreen: MutableState<Screen?> = mutableStateOf(null)

    fun changeScreen(screen: Screen) {
        history.push(screen)
        currentScreen.value = screen
    }

    fun back() {
        history.pop()
        currentScreen.value = history.peek()
    }
}