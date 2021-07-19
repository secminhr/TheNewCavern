package personal.secminhr.cavern.main.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class SessionUserViewModel: CavernViewModel() {
    fun login(onFinished: (Account) -> Unit) {
        viewModelScope.launch {
            try {
                cavernApi.currentUser().let(onFinished)
            } catch (e: CavernError) {
                //empty
            }
        }
    }
}