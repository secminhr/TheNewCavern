package stoneapp.secminhr.cavern.api.requests

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.CavernError
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import java.net.URL
import java.net.UnknownHostException

internal suspend fun Articles(pageLimit: Int): Flow<Pair<Int, List<ArticlePreview>>> = flow {
    var counter = 1
    while (true) {
        val list = getArticle(counter, pageLimit)
        if (list.isEmpty()) {
            break
        }
        emit(counter to list)
        counter++
    }
}.flowOn(Dispatchers.IO)

class ArticlesDataSource: PagingSource<Int, ArticlePreview>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlePreview> {
        val page = params.key?: 1
        return try {
            withContext(Dispatchers.IO) {
                val list = getArticle(page, 10)
                LoadResult.Page(
                    data = list,
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (list.isEmpty()) null else page + 1
                )
            }
        } catch(e: CavernError) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ArticlePreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}


private fun getArticle(page: Int, pageLimit: Int): List<ArticlePreview> =
    runCatching {
        val url = URL("${Cavern.host}/ajax/posts.php?page=$page&limit=$pageLimit")
        url.openStream().inputAs<JsonObject>()
    }.getOrElse {
        throw when (it) {
            is UnknownHostException -> NoConnectionError()
            else -> NetworkError()
        }
    }.run {
        this["posts"]?.let {
            Cavern.json.decodeFromJsonElement<Array<ArticlePreview>>(it)
        }.toList()
    }

private fun <T> Array<T>?.toList(): List<T> {
    if (this == null) {
        return emptyList()
    }
    val list = mutableListOf<T>()
    list.addAll(this)
    return list

}