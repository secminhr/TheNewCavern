package stoneapp.secminhr.cavern.api.results

import com.android.volley.Request
import com.android.volley.RequestQueue
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getStringFromKey


class SessionUser(private val queue: RequestQueue):
        User("", "", queue) {

    override fun get(onSuccess: (User) -> Unit, onFailure: (CavernError) -> Unit) {
        val userURL = "${Cavern.host}/ajax/user.php"
        val subRequest = CavernJsonObjectRequest(Request.Method.GET, userURL, null,
                {
                    val username = it.getStringFromKey("username_key")
                    val nickname = it.getStringFromKey("author_key")
                    val email = it.getStringFromKey("email_key")
                    val imageLink = "https://www.gravatar.com/avatar/${it.getStringFromKey("hash_key")}?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500"
                    val roleLevel = it.getIntFromKey("role_key")
                    val postCount = it.getIntFromKey("posts_count_key")
                    RoleDetail(roleLevel, queue).get({ roleDetail ->
                        account = Account(username, nickname, roleDetail.role, imageLink, postCount, email)
                        onSuccess(this)
                    }) {
                        onFailure(it)
                    }
                },
                {
                    onFailure(SessionExpiredError())
                })
        queue.add(subRequest)
    }

}