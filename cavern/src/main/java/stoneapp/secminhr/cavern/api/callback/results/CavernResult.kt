package stoneapp.secminhr.cavern.api.callback.results

import stoneapp.secminhr.cavern.cavernError.CavernError

interface CavernResult<R> {
    fun get(onSuccess: (R) -> Unit, onFailure: (CavernError) -> Unit)
}