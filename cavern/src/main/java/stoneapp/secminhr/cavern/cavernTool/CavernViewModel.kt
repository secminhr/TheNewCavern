package stoneapp.secminhr.cavern.cavernTool

import androidx.lifecycle.ViewModel
import stoneapp.secminhr.cavern.api.Cavern

open class CavernViewModel: ViewModel() {
    protected val cavernApi = Cavern.instance
}