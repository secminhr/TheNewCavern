package personal.secminhr.cavern.main.ui.views.login

import androidx.compose.animation.Crossfade
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.MainActivity
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.LoginViewModel

class LoginScreen: Screen {

    override val content = @Composable {
        var isCredentialWrong: Boolean by remember { mutableStateOf(false) }
        var isLogging: Boolean by remember { mutableStateOf(false) }
        val viewModel: LoginViewModel = viewModel()

        Crossfade(MainActivity.currentAccount) {
            if (it == null) {
                LoginView(isCredentialWrong, isLogging) { username, password ->
                    isLogging = true
                    viewModel.login(username, password, onFinished = { user ->
                        isCredentialWrong = false
                        isLogging = false
                        MainActivity.currentAccount = user
                    }, onWrongCredential = {
                        isCredentialWrong = true
                        isLogging = false
                    })
                }
            } else {
                val username = remember(it) { mutableStateOf(it.username) }
                UserView(username)
            }
        }
    }

    val icon = @Composable {
        Icon(Icons.Default.Article, "Article")
    }

    override val topBarIcons = listOf(icon)
    override val topBarIconActions = listOf(
        { backToPreviousScreen() }
    )
    override val shouldShowBackButton = false
}