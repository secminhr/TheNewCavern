package personal.secminhr.cavern.main.ui.views.login

import androidx.compose.animation.Crossfade
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel

class LoginScreen: Screen() {

    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        appBar {
            iconButton(::backToPreviousScreen) {
                Icon(Icons.Default.Article, "Article")
            }
        }

        var isCredentialWrong: Boolean by remember { mutableStateOf(false) }
        var isLogging: Boolean by remember { mutableStateOf(false) }
        val viewModel = viewModel<CurrentUserViewModel>()
        Crossfade(viewModel.currentUser) {
            if (it == null) {
                LoginView(isCredentialWrong, isLogging) { username, password ->
                    isLogging = true
                    viewModel.login(username, password, onFinish = {
                        isCredentialWrong = false
                        isLogging = false
                    }, onWrongCredential = {
                        isCredentialWrong = true
                        isLogging = false
                    })
                }
            } else {
                val username = remember(it) { mutableStateOf(it.username) }
                UserView(username, showSnackbar)
            }
        }
    }
}