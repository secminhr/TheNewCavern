package personal.secminhr.cavern.main.ui.views.login

import androidx.compose.animation.Crossfade
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel

object LoginScreen: Screen() {

    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        appBar {
            iconButton(::backToPreviousScreen) {
                Icon(Icons.Default.Article, "Article", tint = Color.White)
            }
        }

        var isCredentialWrong by remember { mutableStateOf(false) }
        var isLogging by remember { mutableStateOf(false) }
        val viewModel = viewModel<CurrentUserViewModel>()
        val scope = rememberCoroutineScope()

        Crossfade(viewModel.currentUser) {
            if (it == null) {
                LoginView(isCredentialWrong, isLogging) { username, password ->
                    isLogging = true
                    scope.launch {
                        isCredentialWrong = !viewModel.login(username, password)
                        isLogging = false
                    }
                }
            } else {
                UserView(it)
            }
        }
    }
}