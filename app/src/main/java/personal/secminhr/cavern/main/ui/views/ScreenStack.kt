package personal.secminhr.cavern.main.ui.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

class ScreenStack(initScreen: Screen) {
    private val history = Stack<Screen>()
    var currentScreen: Screen by mutableStateOf(initScreen)

    init {
        changeScreen(initScreen)
    }

    fun changeScreen(screen: Screen) {
        history.push(screen)
        currentScreen = screen
    }

    fun back() {
        history.pop()
        currentScreen = history.peek()
    }
}