package stoneapp.secminhr.cavern.api.results

import com.android.volley.Request
import com.android.volley.RequestQueue
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.GetRoleFailedError
import stoneapp.secminhr.cavern.cavernObject.Role
import stoneapp.secminhr.cavern.cavernService.CavernJsonObjectRequest


class RoleDetail(val level: Int, val queue: RequestQueue): CavernResult<RoleDetail> {

    lateinit var role: Role

    override fun get(onSuccess: (RoleDetail) -> Unit, onFailure: (CavernError) -> Unit) {
        val url = "https://cavern-8e04d.firebaseio.com/authority/$level.json"
        val request = CavernJsonObjectRequest(Request.Method.GET, url, null,
                {
                    role = Role(level, it.getString("name"),
                        it.getBoolean("canPostArticle"),
                        it.getBoolean("canLike"),
                        it.getBoolean("canComment"))
                    onSuccess(this)
                }, {
                    onFailure(GetRoleFailedError())
                }
        )
        queue.add(request)
    }
}