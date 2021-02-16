package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.Account
import java.net.URL

class Author(val username: String): Result<Account> {

    override suspend fun get(): Account = withContext(Dispatchers.IO) {
        val json = runCatching {
            val url = URL("${Cavern.host}/ajax/user.php?username=$username")
            url.openStream().inputAsJson()
        }.getOrThrow()

        Account.fromJson(json)
    }
}