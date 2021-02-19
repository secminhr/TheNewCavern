package stoneapp.secminhr.cavern.api

import android.content.Context
import stoneapp.secminhr.cavern.api.requests.*
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.concurrent.thread

class Cavern private constructor(context: Context) {

    init {
        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(context), CookiePolicy.ACCEPT_ALL))
        }
    }

    suspend fun getArticles(pageLimit: Int = 10) = Articles(pageLimit)
    suspend fun getArticleContent(preview: ArticlePreview) = ArticleContent(preview)
    suspend fun getAuthor(username: String) = User(username)
    suspend fun getComments(id: Int) = Comments(id)
    suspend fun sendComment(pid: Int, content: String) = SendComment(pid, content)
    suspend fun login(username: String, password: String) = Login(username, password)
    suspend fun currentUser() = User()
    suspend fun logout() = Logout()
    suspend fun like(id: Int) = Like(id)
    suspend fun publishArticle(title: String, content: String) = SendArticle(title=title, content=content)
    suspend fun editArticle(pid: Int, title: String, content: String) = SendArticle(pid, title, content)
    suspend fun deleteArticle(pid: Int) = DeleteArticle(pid)

    companion object {
        var instance: Cavern? = null
            private set

        fun initialize(context: Context) {
            instance = Cavern(context.applicationContext)
        }

        const val host = "https://stoneapp.tech/cavern"
    }
}