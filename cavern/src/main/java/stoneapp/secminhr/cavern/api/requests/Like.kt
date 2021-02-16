package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernService.XSRFTokenGenerator
import stoneapp.secminhr.cavern.cavernService.XSRfHeader
import java.net.URL

class Like(val id: Int): Result<Unit> {

    override suspend fun get() = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL("${Cavern.host}/ajax/like.php?pid=$id")
            url.openConnection()
        }.getOrThrow().runCatching {
            val header = XSRfHeader(XSRFTokenGenerator.token)
            setRequestProperty(header.first, header.second)
            doOutput = true
            doInput = true
            this.getInputStream().inputAsJson()
        }

        Unit
    }
}