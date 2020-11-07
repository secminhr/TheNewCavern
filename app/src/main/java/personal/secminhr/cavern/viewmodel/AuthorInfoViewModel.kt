package personal.secminhr.cavern.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.results.Author
import stoneapp.secminhr.coroutine.awaitAuthorInfo

class AuthorInfoViewModel: ViewModel() {

    fun getAuthorInfo(username: String, onComplete: (Author?) -> Unit) {
        viewModelScope.launch {
            Cavern.instance?.awaitAuthorInfo(username)?.let(onComplete)
        }
    }
}