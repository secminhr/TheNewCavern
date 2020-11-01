package stoneapp.secminhr.cavern.cavernService

import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

open class CavernJsonObjectRequest(method: Int,
                              url: String,
                              jsonRequest: JSONObject?,
                              val listener: Listener<JSONObject>,
                              errorListener: ErrorListener? = null): JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

    override fun getHeaders() =
        super.getHeaders().plus("x-xsrf-token" to XSRFTokenGenerator.token).toMutableMap()

}