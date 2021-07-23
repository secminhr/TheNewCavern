package personal.secminhr.cavern.main.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

val Context.dataStore by preferencesDataStore("Cavern")
class EditorViewModel(context: Context): CavernViewModel() {
    private val dataStore = context.dataStore

    fun getTitle(): Flow<String> = dataStore.data.map {
        it[titleKey]?: ""
    }

    fun getContent(): Flow<TextFieldValue> = dataStore.data.map {
        TextFieldValue(text = it[contentKey]?: "", TextRange(it[cursorPosKey]?: 0))
    }

    fun save(title: String, content: TextFieldValue, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            dataStore.edit {
                it[titleKey] = title
                it[contentKey] = content.text
                it[cursorPosKey] = content.selection.min
            }
            onSuccess()
        }
    }

    val sendingState = mutableStateOf(SendState.Resting)
    fun send(title: String, content: String, onSuccess: () -> Unit = {}) {
        sendingState.value = SendState.Sending
        viewModelScope.launch {
            val result = cavernApi.publishArticle(title, content)
            sendingState.value = if (result) SendState.Success else SendState.Failed
            if (result) {
                save("", TextFieldValue())
                onSuccess()
            }
        }
    }

    fun edit(pid: Int, title: String, content: String, onSuccess: () -> Unit = {}) {
        sendingState.value = SendState.Sending
        viewModelScope.launch(Dispatchers.IO) {
            val success = cavernApi.editArticle(pid, title, content)
            sendingState.value = if (success) SendState.Success else SendState.Failed
            if (success) {
                onSuccess()
            }
        }
    }

    enum class SendState {
        Resting, Sending, Success, Failed
    }

    companion object {
        private val titleKey = stringPreferencesKey("title")
        private val contentKey = stringPreferencesKey("content")
        private val cursorPosKey = intPreferencesKey("cursor")

        val factory: (Context) -> ViewModelProvider.Factory = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return EditorViewModel(it) as T
                }
            }
        }
    }
}