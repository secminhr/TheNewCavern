package personal.secminhr.cavern

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.coroutine.awaitLogout

class LogoutViewModel: ViewModel() {

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = Cavern.instance?.awaitLogout()
            if (result != null) {
                onSuccess()
            }
        }
    }
}