package stoneapp.secminhr.cavern.api.callback

import stoneapp.secminhr.cavern.api.CavernTask
import stoneapp.secminhr.cavern.api.callback.results.CavernResult
import stoneapp.secminhr.cavern.cavernError.CavernError

class CavernCallback<R: CavernResult<R>>(private val result: R): CavernTask<R, CavernCallback<R>> {

    private val onSuccessListener: MutableList<(R) -> Unit> = mutableListOf()
    private val onFailureListener: MutableList<(CavernError) -> Unit> = mutableListOf()

    fun addOnSuccessListener(listener: (R) -> Unit) {
        onSuccessListener += listener
    }

    fun addOnFailureListener(listener: (CavernError) -> Unit) {
        onFailureListener += listener
    }

    override fun execute() {
        result.get(onSuccess = {
            for(listener in onSuccessListener) {
                listener(it)
            }
        }, onFailure = {
            for(listener in onFailureListener) {
                listener(it)
            }
        })
    }
}