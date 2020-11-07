package stoneapp.secminhr.cavern.api.results

import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.RequestQueue
import org.json.JSONException
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.NotExistsError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.Role
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getStringFromKey

class Author(val username: String, private val queue: RequestQueue): CavernResult<Author> {

    var account = Account(username, "", Role(-1, "", false, false, false), "", 0)

    override fun get(onSuccess: (Author) -> Unit, onFailure: (CavernError) -> Unit) {

        val url = "${Cavern.host}/ajax/user.php?username=$username"
        val request = CavernJsonObjectRequest(Request.Method.GET, url, null,
                {
                    val email = try {
                        it.getStringFromKey("email_key")
                    } catch (e: JSONException) { //value not exists
                        null
                    }
                    RoleDetail(it.getIntFromKey("role_key"), queue).get({ detail ->
                        account = Account(username,
                                it.getStringFromKey("author_key"),
                                detail.role,
                                "https://www.gravatar.com/avatar/${it.getStringFromKey("hash_key")}?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500",
                                it.getIntFromKey("posts_count_key"),
                                email
                        )
                        onSuccess(this)
                    }) {
                        onFailure(it)
                    }
                },
                {
                    when {
                        it is NetworkError -> onFailure(stoneapp.secminhr.cavern.cavernError.NetworkError())
                        it is NoConnectionError -> onFailure(stoneapp.secminhr.cavern.cavernError.NoConnectionError())
                        it.networkResponse.statusCode == 404 -> onFailure(NotExistsError())
                    }
                }
        )
        queue.add(request)
    }
}