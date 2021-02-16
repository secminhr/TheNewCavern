package personal.secminhr.cavern.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class UserInfoViewModel: CavernViewModel() {

    fun getAuthorInfo(username: String): MutableState<Account?> {
        val account: MutableState<Account?> = mutableStateOf(null)

        viewModelScope.launch {
            account.value = cavernApi.getAuthor(username)
        }

        return account
    }
}