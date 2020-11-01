package stoneapp.secminhr.cavern.api.results

import com.android.volley.*
import com.google.gson.JsonParser
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernService.CavernStringRequest
import kotlin.properties.Delegates

class SendComment(private val pid: Int,
                  private val content: String,
                  private val queue: RequestQueue): CavernResult<SendComment> {

    var commentId by Delegates.notNull<Int>()
        private set
    var succeed = false
        private set

    override fun get(onSuccess: (SendComment) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/ajax/comment.php"
        val requestMap = mapOf(
                "pid" to pid.toString(),
                "content" to content
        )
        val request = object: CavernStringRequest(Request.Method.POST, url,
                Response.Listener {
                    val response = JsonParser().parse(it).asJsonObject
                    succeed = response.get("status").asBoolean
                    commentId = response.get("comment_id").asInt
                    onSuccess(this)
                },
                Response.ErrorListener {
                    when(it) {
                        is NoConnectionError -> onFailure(stoneapp.secminhr.cavern.cavernError.NoConnectionError())
                        is NetworkError -> onFailure(stoneapp.secminhr.cavern.cavernError.NetworkError())
                    }
                }) {

            override fun getParams(): MutableMap<String, String> {
                return requestMap.toMutableMap()
            }
        }
        queue.add(request)
    }
}