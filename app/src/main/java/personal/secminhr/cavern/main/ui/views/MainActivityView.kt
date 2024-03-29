package personal.secminhr.cavern.main.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import personal.secminhr.cavern.main.viewmodel.ArticlesListViewModel

@Composable
fun MainActivityView(screen: Screen) {
    viewModel<ArticlesListViewModel>().listState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Crossfade(screen) { screen ->
        Scaffold(scaffoldState = scaffoldState, floatingActionButton = screen.fabButton) {
            screen.Screen(showSnackbar = {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(it)
                }
            }, scope)
        }
    }
}

