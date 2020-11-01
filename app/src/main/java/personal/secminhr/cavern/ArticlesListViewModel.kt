package personal.secminhr.cavern

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernTool.CavernViewModel
import stoneapp.secminhr.coroutine.awaitArticles
import kotlin.math.ceil

class ArticlesListViewModel: CavernViewModel() {

    private var articlesData: MutableState<List<ArticlePreview>> = mutableStateOf(listOf())

    fun getArticlesPreview(): State<List<ArticlePreview>> {
        viewModelScope.launch {
            val result = cavernApi.awaitArticles(1, 10)
            articlesData.value = articlesData.value + result.articles

            val total = result.totalPageCount
            val pages = ceil(total / 10f).toInt()
            for(i in 2..pages) {
                val page = cavernApi.awaitArticles(i, 10)
                articlesData.value = articlesData.value + page.articles
            }
        }

        return articlesData
    }
}