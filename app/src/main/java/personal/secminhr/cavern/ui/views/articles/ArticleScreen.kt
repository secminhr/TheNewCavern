package personal.secminhr.cavern.ui.views.articles


import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.MainActivity.Companion.articleContentScreen
import personal.secminhr.cavern.MainActivity.Companion.editorScreen
import personal.secminhr.cavern.MainActivity.Companion.loginScreen
import personal.secminhr.cavern.mainActivity
import personal.secminhr.cavern.ui.style.purple500
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.viewmodel.ArticlesListViewModel

open class ArticleScreen: Screen {

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override val content = @Composable {
        val viewModel = viewModel<ArticlesListViewModel>()
        ArticleList(list = viewModel.getArticlesPreview(onNoConnection = { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) },
                                                        onNetworkError = { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }),
                state = viewModel.listState!!,
                onItemClicked = { navigateTo(articleContentScreen(it)) },
                onLikeClicked = {
                    if (MainActivity.hasCurrentUser) {
                        viewModel.likeArticle(it)
                    }
                }
        )
        if (MainActivity.currentAccount != null) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                ExtendedFloatingActionButton(text = { Text("New Article", color = Color.White) },
                                            onClick = { navigateTo(editorScreen) },
                                            backgroundColor = purple500,
                                            icon = { Icon(Icons.Default.Add, "Add", tint = Color.White) })
            }
        }
    }

    private val userIcon = @Composable {
        if (MainActivity.currentAccount != null) {
            val image = MainActivity.currentAccount!!.avatarLink
            val imageView = ImageView(LocalContext.current)
            Glide.with(LocalContext.current)
                .asDrawable()
                .load(image)
                .into(imageView)
            AndroidView({ imageView }, modifier = Modifier.clip(CircleShape).size(24.dp))

        } else {
            Icon(Icons.Default.AccountCircle, null)
        }
    }

    override val topBarIcons: List<@Composable () -> Unit>
        get() {
            return listOf(userIcon)
        }

    override val topBarIconActions: List<() -> Unit> = listOf(
        { navigateTo(loginScreen) }
    )
    override val shouldShowBackButton = false
}