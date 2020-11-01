package personal.secminhr.cavern.ui.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.FocusManagerAmbient
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import personal.secminhr.cavern.R

@ExperimentalFocus
@Composable
fun LoginView(isCredentialWrong: Boolean, isLogging: Boolean, loginClicked: (String, String) -> Unit) {
    val logo = loadVectorResource(id = R.drawable.logo_img)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordFocus = FocusRequester()
    val focusManager = FocusManagerAmbient.current
    Column(modifier = Modifier.padding(16.dp)) {
        if (logo.resource.resource != null) {
            Image(painter = VectorPainter(asset = logo.resource.resource!!),
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(bottom = 8.dp),
            contentScale = ContentScale.FillWidth)
        }
        OutlinedTextField(value = username,
                          onValueChange = { username = it },
                          label = { Text("Username") },
                          imeAction = ImeAction.Next,
                          onImeActionPerformed = {_, _, -> passwordFocus.requestFocus()},
                          isErrorValue = isCredentialWrong,
                          modifier = Modifier.align(Alignment.CenterHorizontally)
                                             .padding(bottom = 8.dp))
        OutlinedTextField(value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            imeAction = ImeAction.Go,
                            onImeActionPerformed = {_, _ ->
                                loginClicked(username, password)
                                focusManager.clearFocus()
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardType = KeyboardType.Password,
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