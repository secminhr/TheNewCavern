package stoneapp.secminhr.cavern.api

import stoneapp.secminhr.cavern.api.callback.results.CavernResult

interface CavernTask<R: CavernResult<R>, out T: CavernTask<R, T>> {
    fun setup(scope: T.() -> Unit): CavernTask<R, T> {
        scope(this as T)
        return this
    }

    fun execute()
}