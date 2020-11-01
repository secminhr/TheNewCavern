package stoneapp.secminhr.cavern.cavernService

import android.util.Base64
import kotlin.random.Random
import kotlin.random.nextUInt

class XSRFTokenGenerator {
    companion object {
        val token: String by lazy {
            Base64.encodeToString(Random.nextUInt().toString().toByteArray(), Base64.NO_WRAP)
        }
    }
}