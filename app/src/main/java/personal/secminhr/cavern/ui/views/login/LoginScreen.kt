package personal.secminhr.cavern.ui.views.login

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.runtime.*
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.viewinterop.viewModel
import personal.secminhr.cavern.viewmodel.LoginViewModel
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.ui.views.ScreenStack

class LoginScreen: Screen {

    @ExperimentalFocus
    override val content = @Composable {
        var isCredentialWrong: Boolean by remember { mutableStateOf(false) }
        var isLogging: Boolean by remember { mutableStateOf(false) }
        val viewModel: LoginViewModel = viewModel()

        Crossfade(MainActivity.currentAccount) {
            if (it == null) {

                LoginView(isCredentialWrong, isLogging) { username, password ->
                    isLogging = true
                    viewModel.login(username, password, onFinished = { user ->
                        isLogging = false
                        MainActivity.currentAccount = user
                    }, onWrongCredential = {
                        isCredentialWrong = true
                        isLogging = false
                    })
                }
            } else {
                CurrentUserView(it)
            }
        }
    }
    override val topBarIcon = @Composable {
        Icon(Icons.Default.Article)
    }
    override val topBarIconAction = { screenStack: ScreenStack ->
        screenStack.back()
    }
}