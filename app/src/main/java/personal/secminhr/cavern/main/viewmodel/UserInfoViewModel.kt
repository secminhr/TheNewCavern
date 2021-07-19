package personal.secminhr.cavern.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class UserInfoViewModel: CavernViewModel() {

    fun getAuthorInfo(username: String, onError: (CavernError) -> Unit = {}): MutableState<Account?> {
        val account: MutableState<Account?> = mutableStateOf(null)

        viewModelScope.launch {
            try {
                account.value = cavernApi.getUserInfo(username)
            } catch (e: CavernError) {
                onError(e)
            }
        }

        return account
    }
}