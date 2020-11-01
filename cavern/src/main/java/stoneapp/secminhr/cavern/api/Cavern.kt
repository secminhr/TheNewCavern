package stoneapp.secminhr.cavern.api

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import stoneapp.secminhr.cavern.api.results.*
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.concurrent.thread

class Cavern private constructor(private val context: Context) {

    private val requestQueue = RequestQueue(NoCache(), BasicNetwork(HurlStack())).apply { start() }

    init {
        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(context), CookiePolicy.ACCEPT_ALL))
        }
    }

    fun getArticles(page: Int, limit: Int = 10): CavernTask<Articles> {
        return CavernTask(Articles(requestQueue, page, limit))
    }

    fun getArticleContent(preview: ArticlePreview): CavernTask<ArticleContent> {
        return CavernTask(ArticleContent(requestQueue, preview))
    }

    fun getAuthor(username: String): CavernTask<Author> {
        return CavernTask(Author(username, requestQueue))
    }

    fun getComments(id: Int): CavernTask<Comments> {
        return CavernTask(Comments(id, requestQueue))
    }

    fun sendComment(pid: Int, content: String): CavernTask<SendComment> {
        return CavernTask(SendComment(pid, content, requestQueue))
    }

    fun login(username: String, password: String): CavernTask<User> {
        return CavernTask(User(username, password, requestQueue))
    }

    fun login(): CavernTask<User> {
        return CavernTask(SessionUser(requestQueue))
    }

    fun logout(): CavernTask<LogoutResult> {
        return CavernTask(LogoutResult(requestQueue))
    }

    fun like(id: Int): CavernTask<LikeResult> {
        return CavernTask(LikeResult(id, requestQueue))
    }

    fun publishArticle(title: String, content: String): CavernTask<PublishArticle> {
        return CavernTask(PublishArticle(title, content, requestQueue))
    }

    fun getRoleDetail(level: Int, queue: RequestQueue): CavernTask<RoleDetail> {
        return CavernTask(RoleDetail(level, queue))
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