package stoneapp.secminhr.cavern.api

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import kotlinx.coroutines.flow.Flow
import stoneapp.secminhr.cavern.api.callback.CavernCallback
import stoneapp.secminhr.cavern.api.callback.results.SendComment
import stoneapp.secminhr.cavern.api.requests.*
import stoneapp.secminhr.cavern.cavernObject.*
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.concurrent.thread

class Cavern private constructor(context: Context) {

    private val requestQueue = RequestQueue(NoCache(), BasicNetwork(HurlStack())).apply { start() }

    init {
        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(context), CookiePolicy.ACCEPT_ALL))
        }
    }

    suspend fun getArticles(pageLimit: Int = 10): Flow<Pair<Int, List<ArticlePreview>>> {
        return Articles(pageLimit).get()
    }

    suspend fun getArticleContent(preview: ArticlePreview): Article {
        return ArticleContent(preview).get()
    }

    suspend fun getAuthor(username: String): Account {
        return Author(username).get()
    }

    suspend fun getComments(id: Int): List<Comment> {
        return Comments(id).get()
    }

    fun sendComment(pid: Int, content: String): CavernTask<SendComment, CavernCallback<SendComment>> {
        return CavernCallback(SendComment(pid, content, requestQueue))
    }

    suspend fun login(username: String, password: String): Boolean {
        return Login(username, password).get()
    }

    suspend fun currentUser(): Account {
        return CurrentUser().get()
    }

    suspend fun roleDetail(level: Int): Role {
        return RoleDetail(level).get()
    }

    suspend fun logout(): Boolean {
        return Logout().get()
    }

    suspend fun like(id: Int) {
        return Like(id).get()
    }

    suspend fun publishArticle(title: String, content: String): Boolean {
        return PublishArticle(title, content).get()
    }

    companion object {
        var instance: Cavern? = null
            private set

        fun initialize(context: Context) {
            instance = Cavern(context.applicationContext)
        }

        const val host = "https://stoneapp.tech/cavern"
    }
}