package stoneapp.secminhr.cavern.cavernTool

import androidx.lifecycle.ViewModel
import stoneapp.secminhr.cavern.api.Cavern

open class CavernViewModel: ViewModel() {
    protected val cavernApi: Cavern

    init {
        if (Cavern.instance == null) {
            throw Exception("You must call Cavern.initialize() first")
        }
        cavernApi = Cavern.instance!!
    }
}