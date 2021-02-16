package stoneapp.secminhr.cavern.cavernObject

import com.google.gson.JsonObject
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getStringFromKey

data class Account(
    val username: String,
    val nickname: String,
    val role: Role,
    val avatarLink: String,
    val postCount: Int,
    val email: String? = null //This is used to indicate whether it is the current user
) {
    companion object {
        val Empty = Account("", "", Role.Empty, "", -1)

        suspend fun fromJson(json: JsonObject): Account {
            val name = json.getStringFromKey("username_key")
            val nickname = json.getStringFromKey("author_key")
            val email = try {
                json.getStringFromKey("email_key")
            } catch (e: Exception) {
                null
            }
            val imageLink = "https://www.gravatar.com/avatar/${json.getStringFromKey(
                "hash_key"
            )}?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500"
            val level = json.getIntFromKey("role_key")
            val postCount = json.getIntFromKey("posts_count_key")
            val role = Cavern.instance?.roleDetail(level) ?: Role.Empty

            return Account(name, nickname, role, imageLink, postCount, email)
        }
    }
}