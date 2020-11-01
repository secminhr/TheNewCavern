package stoneapp.secminhr.cavern.api

import stoneapp.secminhr.cavern.api.results.CavernResult
import stoneapp.secminhr.cavern.cavernError.CavernError

class CavernTask<R: CavernResult<R>>(private val result: R) {

    private val onSuccessListener: MutableList<(R) -> Unit> = mutableListOf()
    private val onFailureListener: MutableList<(CavernError) -> Unit> = mutableListOf()

    fun addOnSuccessListener(listener: (R) -> Unit): CavernTask<R> {
        onSuccessListener += listener
        return this
    }

    fun addOnFailureListener(listener: (CavernError) -> Unit): CavernTask<R> {
        onFailureListener += listener
        return this
    }

    fun execute() {
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