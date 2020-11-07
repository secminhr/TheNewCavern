package stoneapp.secminhr.coroutine

import kotlinx.coroutines.suspendCancellableCoroutine
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.api.results.*
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Cavern.awaitArticles(page: Int, limit: Int = 10):
        Articles = suspendCancellableCoroutine { continuation ->
    getArticles(page, limit).addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.execute()
}

suspend fun Cavern.awaitArticleContent(preview: ArticlePreview):
        ArticleContent = suspendCancellableCoroutine { continuation ->

    getArticleContent(preview).addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.execute()
}

suspend fun Cavern.awaitLogin(username: String, password: String): User
        = suspendCancellableCoroutine { continuation ->
    login(username, password).addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.execute()
}

suspend fun Cavern.awaitLogin(): User = suspendCancellableCoroutine { continuation ->
    login().addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.execute()
}

suspend fun Cavern.awaitLogout(): LogoutResult? = suspendCancellableCoroutine { continuation ->
    logout().addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resume(null)
    }.execute()
}

suspend fun Cavern.awaitAuthorInfo(username: String): Author = suspendCancellableCoroutine { continuation ->
    getAuthor(username).addOnSuccessListener {
        continuation.resume(it)
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.execute()
}