package personal.secminhr.cavern

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.WrongCredentialError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.coroutine.awaitLogin

class LoginViewModel: ViewModel() {

    fun login(username: String, password: String, onFinished: (Account) -> Unit, onWrongCredential: () -> Unit) {
        viewModelScope.launch {
            try {
                Cavern.instance?.awaitLogin(username, password)?.let {
                    onFinished(it.account)
                }
            } catch (e: WrongCredentialError) {
                onWrongCredential()
            }
        }
    }
}