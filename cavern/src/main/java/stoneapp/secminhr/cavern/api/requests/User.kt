package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.Role
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

internal suspend fun Login(username: String, password: String): Boolean {
    if(username.isBlank() || password.isBlank()) {
        return false
    }

    return withContext(Dispatchers.IO) {
        val data: ByteArray
        runCatching {
            val url = URL("${Cavern.host}/login.php")
            url.openConnectionXSRF()
        }.getOrElse {
            return@withContext false
        }.also {
            it.instanceFollowRedirects = false
            it.requestMethod = "POST"
            it.doOutput = true
            it.doInput = true
            data = "username=$username&password=$password".toByteArray()
            it.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            it.setFixedLengthStreamingMode(data.size)
        }.runCatching {
            outputStream.write(data)
            this
        }.getOrElse {
            return@withContext false
        }.runCatching {
            try {
                getHeaderField("Location")?.let {
                    if (it.contains("ok")) {
                        //already logged in
                        return@withContext true
                    } else if (it.contains("err")) {
                        return@withContext false
                    }
                }
                if (responseCode == 302) {
                    val location = getHeaderField("location")
                    location?.contains("ok") ?: false
                } else {
                    false
                }
            } catch (e: Exception) {
                return@withContext false
            }
            false
        }.getOrElse {
            false
        }
    }
}

internal suspend fun Logout(): Boolean = withContext(Dispatchers.IO) {
    val connection = runCatching {
        val url = URL("${Cavern.host}/login.php?logout")
        val c = url.openConnectionXSRF()
        c.connect()
        c
    }.getOrElse {
        return@withContext false
    }

    connection.responseCode == HttpURLConnection.HTTP_OK
}

internal suspend fun User(username: String? = null): Account = withContext(Dispatchers.IO) {
    val data = runCatching {
        val url = if (username != null)
            URL("${Cavern.host}/ajax/user.php?username=$username")
        else
            URL("${Cavern.host}/ajax/user.php")

        url.openStream().inputAs<AccountData>()
    }.getOrElse {
        throw when (it) {
            is UnknownHostException -> throw NoConnectionError()
            else -> NetworkError()
        }
    }

    runCatching {
        RoleDetail(data.level)
    }.getOrThrow().run {
        Account(
            data.username,
            data.nickname,
            data.postCount,
            data.email,
            "https://www.gravatar.com/avatar/${data.hash}?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500",
            this
        )
    }
}

private suspend fun RoleDetail(level: Int): Role = withContext(Dispatchers.IO) {
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

@Serializable
private data class AccountData(
    val username: String,
    @SerialName("name") val nickname: String,
    val hash: String,
    @SerialName("posts_count") val postCount: Int,
    val level: Int,
    val email: String? = null //This is used to indicate whether it is the current user
)