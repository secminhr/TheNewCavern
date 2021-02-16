package stoneapp.secminhr.cavern.cavernService

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

open class CavernStringRequest(method: Int,
                          url: String,
                          val listener: Response.Listener<String>,
                          errorListener: Response.ErrorListener? = null):
        StringRequest(method, url, listener, errorListener) {

    override fun getHeaders() = super.getHeaders() + XSRfHeader(XSRFTokenGenerator.token)
}