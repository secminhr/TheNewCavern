package stoneapp.secminhr.cavern.api.results

import android.util.Log
import com.android.volley.*
import org.jsoup.Jsoup
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.getStringFromMap
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.EmptyPasswordError
import stoneapp.secminhr.cavern.cavernError.EmptyUsernameError
import stoneapp.secminhr.cavern.cavernError.WrongCredentialError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.Role
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest
import stoneapp.secminhr.cavern.cavernService.CavernStringRequest
import stoneapp.secminhr.cavern.getIntFromKey
import stoneapp.secminhr.cavern.getStringFromKey

open class User(
    val username: String,
    private val password: String,
    private val requestQueue: RequestQueue
): CavernResult<User> {

    var account = Account("", "", Role(8, "", false, false, false), "", 0, "")
    lateinit var customToken: String

    override fun get(onSuccess: (User) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/login.php"
        if(username == "") {
            onFailure(EmptyUsernameError())
        }
        if(password == "") {
            onFailure(EmptyPasswordError())
        }
        val request = object: CavernStringRequest(Request.Method.POST, url,
                Response.Listener {
                    isLoggedIn {
                        if(it) {
                            val userURL = "${Cavern.host}/ajax/user.php"
                            val subRequest = CavernJsonObjectRequest(Method.GET, userURL, null,
                                    Response.Listener {
                                        val name = it.getStringFromKey("username_key")
                                        val nickname = it.getStringFromKey("author_key")
                                        val email = it.getStringFromKey("email_key")
                                        val imageLink = "https://www.gravatar.com/avatar/${it.getStringFromKey(
                                                "hash_key"
                                        )}?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500"
                                        val level = it.getIntFromKey("role_key")
                                        val postCount = it.getIntFromKey("posts_count_key")
                                        RoleDetail(level, requestQueue).get({ detail ->
                                            account = Account(name, nickname, detail.role, imageLink, postCount, email)
                                            onSuccess(this)
                                        }) {
                                            onFailure(it)
                                        }
                                    })
                            requestQueue.add(subRequest)
                        } else {
                            Log.e("User", "OnError login failed")
                            onFailure(WrongCredentialError())
                        }
                    }
                },
                Response.ErrorListener {
                    Log.e("User", "OnError")
                    when(it) {
                        is NoConnectionError -> onFailure(stoneapp.secminhr.cavern.cavernError.NoConnectionError())
                        is NetworkError -> onFailure(stoneapp.secminhr.cavern.cavernError.NetworkError())
                    }
                }) {

            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf("username" to username,
                        "password" to password)
            }
        }
        requestQueue.add(request)
    }

    private fun isLoggedIn(ans: (Boolean) -> Unit) {
        val url = "${Cavern.host}/index.php?ok=login"
        val request = CavernStringRequest(Request.Method.GET, url, Response.Listener {
            val document = Jsoup.parse(it)
            ans(document.body().selectFirst(getStringFromMap("login")) == null)
        }, Response.ErrorListener {
            ans(false)
        })
        requestQueue.add(request)
    }
}