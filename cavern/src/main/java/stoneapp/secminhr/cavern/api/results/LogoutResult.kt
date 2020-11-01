package stoneapp.secminhr.cavern.api.results

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.LogoutFailedError
import stoneapp.secminhr.cavern.cavernService.CavernStringRequest

class LogoutResult(private val queue: RequestQueue): CavernResult<LogoutResult> {

    override fun get(onSuccess: (LogoutResult) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "${Cavern.host}/login.php?logout"
        val request = object: CavernStringRequest(Method.GET, url, Response.Listener {
        }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                val location = response?.headers?.get("axios-location")
                location?.let {
                    if(it == "index.php?ok=logout") {
                        onSuccess(this@LogoutResult)
                    } else {
                        onFailure(LogoutFailedError())
                    }
                }
                return super.parseNetworkResponse(response)
            }
        }
        queue.add(request)
    }
}