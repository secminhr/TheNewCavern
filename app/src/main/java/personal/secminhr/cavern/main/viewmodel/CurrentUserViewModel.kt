package personal.secminhr.cavern.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class CurrentUserViewModel: CavernViewModel() {
    var currentUser: Account? by mutableStateOf(null)

    fun sessionLogin() {
        viewModelScope.launch {
            try {
                cavernApi.currentUser().let {
                    currentUser = it
                }
            } catch (e: CavernError) {
                //empty
            }
        }
    }

    suspend fun login(username: String, password: String): Boolean {
        val succeed = cavernApi.login(username, password)
        if (succeed) {
            currentUser = cavernApi.currentUser()
        }
        return succeed
    }

    fun logout() {
        viewModelScope.launch {
            val result = cavernApi.logout()
            if (result) {
                currentUser = null
            }
        }
    }
}