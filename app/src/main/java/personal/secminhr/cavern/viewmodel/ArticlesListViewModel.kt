package personal.secminhr.cavern.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel

class ArticlesListViewModel: CavernViewModel() {

    var listState: LazyListState? = null
    private var articles = mutableStateListOf<ArticlePreview>()
    private var articlesData = mutableMapOf<Int, List<ArticlePreview>>()

    fun getArticlesPreview(): SnapshotStateList<ArticlePreview> {
        viewModelScope.launch {
            cavernApi.getArticles(10).collect { (page, list) ->
                articlesData[page] = list

                if (articles.isNotEmpty()) {
                    articles.clear()
                }

                articles.addAll(
                    articlesData.toSortedMap().flatMap { it.value }
                )
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