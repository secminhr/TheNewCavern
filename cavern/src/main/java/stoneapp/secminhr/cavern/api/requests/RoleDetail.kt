package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Role
import java.net.URL
import java.net.UnknownHostException

suspend fun RoleDetail(level: Int): Role = withContext(Dispatchers.IO) {
    runCatching {
        val url = URL("https://cavern-8e04d.firebaseio.com/authority/$level.json")
        url.openStream().inputAs<Role>()
    }.getOrElse {
        throw when(it) {
            is UnknownHostException -> NoConnectionError()
            else -> NetworkError()
        }
    }
}