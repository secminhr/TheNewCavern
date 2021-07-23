package personal.secminhr.cavern.main.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.api.requests.ArticlesDataSource
import stoneapp.secminhr.cavern.cavernError.NetworkError
import stoneapp.secminhr.cavern.cavernError.NoConnectionError
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class ArticlesListViewModel: CavernViewModel() {

    var listState: LazyListState? = null
    private var articles = mutableStateListOf<ArticlePreview>()
    private var articlesData = mutableMapOf<Int, List<ArticlePreview>>()

    val pagedArticles: Flow<PagingData<ArticlePreview>> by lazy {
        Pager(PagingConfig(10)) { ArticlesDataSource() }.flow
    }

    fun getArticlesPaged(): Flow<PagingData<ArticlePreview>> =
        Pager(PagingConfig(10)) { ArticlesDataSource() }.flow

    fun getArticlesPreview(
        onNoConnection: (NoConnectionError) -> Unit = {},
        onNetworkError: (NetworkError) -> Unit = {}
    ): SnapshotStateList<ArticlePreview> {
        viewModelScope.launch {
            try {
                cavernApi.getArticles(10).collect { (page, list) ->
                    articlesData[page] = list

                    if (articles.isNotEmpty()) {
                        articles.clear()
                    }

                    articles.addAll(
                        articlesData.toSortedMap().flatMap { it.value }
                    )
                }
            } catch (e: NoConnectionError) {
                onNoConnection(e)
            } catch (e: NetworkError) {
                onNetworkError(e)
            }
        }

        return articles
    }

    fun likeArticle(id: Int) {
        viewModelScope.launch {
            cavernApi.like(id)
        }
    }
}