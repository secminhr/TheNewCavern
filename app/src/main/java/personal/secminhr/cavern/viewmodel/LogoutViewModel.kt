package personal.secminhr.cavern.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.Cavern

class LogoutViewModel: ViewModel() {

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = Cavern.instance?.logout() ?: false
            if (result) {
                onSuccess()
            }
        }
    }
}