package personal.secminhr.cavern.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class LoginViewModel: CavernViewModel() {

    fun login(username: String, password: String, onFinished: (Account) -> Unit, onWrongCredential: () -> Unit) {
        viewModelScope.launch {
            val succeed = cavernApi.login(username, password)
            if (succeed) {
                cavernApi.currentUser().run(onFinished)
            } else {
                onWrongCredential()
            }
        }
    }
}