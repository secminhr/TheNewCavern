package stoneapp.secminhr.cavern.api.results

import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.RequestQueue
import com.android.volley.Response
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.NoLoginError
import stoneapp.secminhr.cavern.cavernService.CavernStringRequest

class PublishArticle(val title: String, val content: String, val requestQueue: RequestQueue): CavernResult<PublishArticle> {
    override fun get(onSuccess: (PublishArticle) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/post.php"
        val request = object: CavernStringRequest(Method.POST, url,
                Response.Listener {
                    onSuccess(this)
                },
                Response.ErrorListener {
                    when {
                        it is NoConnectionError -> onFailure(stoneapp.secminhr.cavern.cavernError.NoConnectionError())
                        it is NetworkError -> onFailure(stoneapp.secminhr.cavern.cavernError.NetworkError())
                        it.networkResponse.statusCode == 401 -> onFailure(NoLoginError())
                    }
                }) {
            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf("title" to title,
                                    "content" to content,
                                    "pid" to "-1")
            }
        }
        requestQueue.add(request)
    }
}