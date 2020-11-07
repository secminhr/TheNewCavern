package personal.secminhr.cavern.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel
import stoneapp.secminhr.coroutine.awaitArticles
import kotlin.math.ceil

class ArticlesListViewModel: CavernViewModel() {

    var listState: LazyListState? = null
    private var articles: MutableMap<Int, List<ArticlePreview>> = mutableMapOf()
    private var articlesData: MutableState<List<ArticlePreview>> = mutableStateOf(listOf())

    fun getArticlesPreview(): List<ArticlePreview> {
        viewModelScope.launch {
            val result = cavernApi.awaitArticles(1, 10)
            articles.addArticlesWithNotify(1, result.articles, articlesData)

            val total = result.totalPageCount
            val pages = ceil(total / 10f).toInt()
            for(i in 2..pages) {
                val page = cavernApi.awaitArticles(i, 10)
                articles.addArticlesWithNotify(i, page.articles, articlesData)
            }
        }

        return articlesData.value
    }
}

fun <V>MutableMap<Int, List<V>>.addArticlesWithNotify(i: Int, articles: List<V>, data: MutableState<List<V>>) {
    this[i] = articles
    val list = mutableListOf<V>()
    this.keys.sorted().forEach {
        list.addAll(this[it]!!)
    }
    data.value = list
}