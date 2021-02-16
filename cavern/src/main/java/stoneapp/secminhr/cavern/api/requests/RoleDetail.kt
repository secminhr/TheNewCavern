package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.cavernObject.Role
import stoneapp.secminhr.cavern.cavernService.gson
import java.net.URL

class RoleDetail(private val level: Int): Result<Role> {

    override suspend fun get(): Role = withContext(Dispatchers.IO) {
        val json = runCatching {
            val url = URL("https://cavern-8e04d.firebaseio.com/authority/$level.json")
            url.openStream().inputAsJson()
        }.getOrThrow()

        gson.fromJson(json, Role::class.java)
    }
}