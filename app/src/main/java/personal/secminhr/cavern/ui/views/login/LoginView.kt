package personal.secminhr.cavern.ui.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import personal.secminhr.cavern.R

@Composable
fun LoginView(isCredentialWrong: Boolean, isLogging: Boolean, loginClicked: (String, String) -> Unit) {
    val logo = loadVectorResource(id = R.drawable.logo_img)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordFocus = FocusRequester()
    val focusManager = LocalFocusManager.current
    val state = rememberScrollState()
    ScrollableColumn(scrollState = state, modifier = Modifier.padding(16.dp)) {
        if (logo.resource.resource != null) {
            Image(logo.resource.resource!!, "logo",
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(bottom = 8.dp),
            contentScale = ContentScale.FillWidth)
        }

        val scope = rememberCoroutineScope()
        OutlinedTextField(username,
                          onValueChange = {
                              scope.launch {
                                  state.smoothScrollTo(state.maxValue)
                              }
                              username = it
                          },
                          label = { Text("Username") },
                          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                          keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                          isErrorValue = isCredentialWrong,
                          modifier = Modifier.align(Alignment.CenterHorizontally)
                                             .padding(bottom = 8.dp))
        OutlinedTextField(value = password,
                            onValueChange = {
                                scope.launch {
                                    state.smoothScrollTo(state.maxValue)
                                }
                                password = it
                            },
                            label = { Text("Password") },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go, keyboardType = KeyboardType.Password),
                            keyboardActions = KeyboardActions(onGo = {
                                loginClicked(username, password)
                                focusManager.clearFocus()
                            }),
                            visualTransformation = PasswordVisualTransformation(),
                            isErrorValue = isCredentialWrong,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        .padding(bottom = 16.dp)
                                        .focusRequester(passwordFocus))

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