package stoneapp.secminhr.cavern.api

import kotlinx.serialization.json.Json
import stoneapp.secminhr.cavern.api.requests.*
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class Cavern {
    suspend fun getArticles(pageLimit: Int = 10) = Articles(pageLimit)
    suspend fun getArticleContent(preview: ArticlePreview) = ArticleContent(preview)
    suspend fun like(id: Int) = LikeArticle(id)

    suspend fun getComments(id: Int) = Comments(id)
    suspend fun sendComment(pid: Int, content: String) = SendComment(pid, content)
    suspend fun editComment(id: Int, content: String) = EditComment(id, content)

    suspend fun login(username: String, password: String) = Login(username, password)
    suspend fun currentUser() = User()
    suspend fun getUserInfo(username: String) = User(username)
    suspend fun logout() = Logout()

    suspend fun publishArticle(title: String, content: String) = SendArticle(title=title, content=content)
    suspend fun editArticle(pid: Int, title: String, content: String) = SendArticle(pid, title, content)
    suspend fun deleteArticle(pid: Int) = DeleteArticle(pid)

    companion object {
        val instance: Cavern by lazy {
            Cavern()
        }

        internal const val host = "https://stoneapp.tech/cavern"
        internal val json = Json {
            ignoreUnknownKeys = true
        }
    }
}