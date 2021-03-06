package personal.secminhr.cavern.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.ui.style.CavernTheme
import personal.secminhr.cavern.viewmodel.ArticlesListViewModel

@Composable
fun MainActivityView(screen: Screen) {
    viewModel<ArticlesListViewModel>().listState = rememberLazyListState()

    CavernTheme {
        Column {
            Crossfade(screen) {
                it.content()
            }
        }
    }
}

