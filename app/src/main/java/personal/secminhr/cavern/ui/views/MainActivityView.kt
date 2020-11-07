package personal.secminhr.cavern.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.viewModel
import personal.secminhr.cavern.ui.style.CavernTheme
import personal.secminhr.cavern.viewmodel.ArticlesListViewModel

@Composable
fun MainActivityView(screen: Screen) {
    viewModel<ArticlesListViewModel>().listState = rememberLazyListState()

    CavernTheme {
        Column {
            Crossfade(current = screen) {
                it.content()
            }
        }
    }
}

//@Preview(showBackground = true, name = "ArticleScreen")
//@Composable
//fun DefaultPreview() {
//    MainActivityView(MutableLiveData(articleScreen), MutableLiveData(false))
//}

