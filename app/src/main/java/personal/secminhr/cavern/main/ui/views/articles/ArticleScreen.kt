package personal.secminhr.cavern.main.ui.views.articles


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import personal.secminhr.cavern.main.MainActivity.Companion.articleContentScreen
import personal.secminhr.cavern.main.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.main.ui.style.purple500
import personal.secminhr.cavern.main.ui.views.Screen
import personal.secminhr.cavern.main.ui.views.login.LoginScreen
import personal.secminhr.cavern.main.viewmodel.ArticlesListViewModel
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import stoneapp.secminhr.cavern.cavernObject.Account

object ArticleScreen: Screen() {

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @Composable
    override fun Screen(showSnackbar: (String) -> Unit) {
        val currentUserViewModel = viewModel<CurrentUserViewModel>()

        appBar {
            iconButton({ navigateTo(LoginScreen) }) {
                UserIcon(currentUserViewModel.currentUser)
            }
        }

        val viewModel = viewModel<ArticlesListViewModel>()
        val scope = rememberCoroutineScope()
        ArticleList(
            list = viewModel.getArticlesPreview(
                onNoConnection = { showSnackbar(it.message!!) },
                onNetworkError = { showSnackbar(it.message!!) }
            ),
            state = viewModel.listState!!,
            onItemClicked = { navigateTo(articleContentScreen(it)) },
            onLikeClicked = { pid ->
                if (currentUserViewModel.currentUser != null) {
                    scope.launch {
                        val succeed = viewModel.likeArticle(pid)
                        if (succeed) {
                            viewModel.getArticlesPreview(
                                onNoConnection = { showSnackbar(it.message!!) },
                                onNetworkError = { showSnackbar(it.message!!) }
                            )
                        }
                    }
                }
            }
        )

        if (currentUserViewModel.currentUser != null) {
            NewArticleButton {
                navigateTo(editorScreen())
            }
        }
    }

    @Composable
    fun UserIcon(user: Account?) {
        if (user != null) {
            Image(
                painter = rememberImagePainter(user.avatarLink),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp)
            )
        } else {
            Icon(Icons.Default.AccountCircle, null)
        }
    }

    @Composable
    fun NewArticleButton(onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            ExtendedFloatingActionButton(
                text = { Text("New Article", color = Color.White) },
                onClick = onClick,
                backgroundColor = purple500,
                icon = { Icon(Icons.Default.Add, "Add", tint = Color.White) })
        }
    }
}