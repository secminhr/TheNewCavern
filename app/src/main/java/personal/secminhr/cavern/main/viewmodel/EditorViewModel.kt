package personal.secminhr.cavern.main.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Cavern")

class EditorViewModel(context: Context): CavernViewModel() {
    private val dataStore = context.dataStore

    private val title = mutableStateOf("")
    private val content = mutableStateOf(TextFieldValue())


    fun getTitle(): MutableState<String> {
        viewModelScope.launch {
            dataStore.data.collect {
                title.value = it[titleKey] ?: ""
            }
        }

        return title
    }

    fun getContent(): MutableState<TextFieldValue> {
        viewModelScope.launch {
            dataStore.data.collect {
                content.value = TextFieldValue(text = it[contentKey]?: "", TextRange(it[cursorPosKey]?: 0))
            }
        }

        return content
    }

    private fun clearAndSave() {
        clear()
        save()
    }

    private fun clear() {
        title.value = ""
        content.value = TextFieldValue()
    }

    fun save() {
        viewModelScope.launch {
            dataStore.edit {
                it[titleKey] = title.value
                it[contentKey] = content.value.text
                it[cursorPosKey] = content.value.selection.min
            }
        }
    }

    val sendingState = mutableStateOf(SendState.Resting)
    fun send(onSuccess: () -> Unit = {}) {
        sendingState.value = SendState.Sending
        viewModelScope.launch {
            val result = cavernApi.publishArticle(title.value, content.value.text)
            sendingState.value = if (result) SendState.Success else SendState.Failed
            if (result) {
                clearAndSave()
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