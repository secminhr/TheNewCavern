package personal.secminhr.cavern.main.ui.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import personal.secminhr.cavern.R

@Composable
fun LoginView(isCredentialWrong: Boolean, isLogging: Boolean, loginClicked: (String, String) -> Unit) {
    val logo = painterResource(id = R.drawable.logo_img)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showingPassword by remember { mutableStateOf(false) }

    val passwordFocus = FocusRequester()
    val focusManager = LocalFocusManager.current

    val state = rememberScrollState()
    LaunchedEffect(state.maxValue) {
        state.animateScrollTo(state.maxValue)
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(state)) {
        Image(
            painter = logo, "logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.FillWidth)

        OutlinedTextField(
            username,
            onValueChange = {
                username = it
            },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                passwordFocus.requestFocus()
            }),
            leadingIcon = {
                Icon(Icons.Default.Person, "username")
            },
            isError = isCredentialWrong,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp))

        OutlinedTextField(
            password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go, autoCorrect = false),
            keyboardActions = KeyboardActions(onGo = {
                loginClicked(username, password)
                focusManager.clearFocus()
            }),
            visualTransformation = if (showingPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isCredentialWrong,
            leadingIcon = {
                Icon(Icons.Default.Lock, "password")
            },
            trailingIcon = {
                IconToggleButton(checked = showingPassword, onCheckedChange = { showingPassword = it }) {
                    if (showingPassword) {
                        Icon(Icons.Default.VisibilityOff, "hide")
                    } else {
                        Icon(Icons.Default.Visibility, "see")
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .focusRequester(passwordFocus)
        )

        if (isLogging) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(onClick = {
                loginClicked(username, password)
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Login")
            }
        }
    }
}