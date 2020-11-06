package personal.secminhr.cavern.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.results.User
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.coroutine.awaitLogin

class SessionUserViewModel: ViewModel() {
    fun login(onFinished: (User) -> Unit) {
        viewModelScope.launch {
            try {
                Cavern.instance?.awaitLogin()?.let {
                    onFinished(it)
                }
            } catch (e: SessionExpiredError) {
                //empty
            }
        }
    }
}