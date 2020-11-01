package stoneapp.secminhr.cavern.cavernError

open class CavernError(message: String): Exception(message) {
    constructor() : this("")
}