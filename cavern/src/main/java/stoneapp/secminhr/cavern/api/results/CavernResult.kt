package stoneapp.secminhr.cavern.api.results

import stoneapp.secminhr.cavern.cavernError.CavernError

interface CavernResult<R: CavernResult<R>> {
    fun get(onSuccess: (R) -> Unit, onFailure: (CavernError) -> Unit)
}