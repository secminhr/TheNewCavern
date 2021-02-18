package stoneapp.secminhr.cavern.api.requests

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.Account
import java.lang.reflect.Type
import java.net.URL
import java.net.UnknownHostException

suspend fun User(username: String? = null): Account = withContext(Dispatchers.IO) {
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

data class AccountData(
    val username: String,
    @SerializedName("name") val nickname: String,
    val hash: String,
    @SerializedName("posts_count") val postCount: Int,
    val level: Int,
    @JsonAdapter(NullableEmailAdapter::class) val email: String? = null //This is used to indicate whether it is the current user
)

class NullableEmailAdapter: JsonDeserializer<String?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        return json?.asString
    }
}