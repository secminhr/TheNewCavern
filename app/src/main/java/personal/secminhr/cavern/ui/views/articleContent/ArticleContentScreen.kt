package personal.secminhr.cavern.ui.views.articleContent

import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import personal.secminhr.cavern.MainActivity.Companion.articleScreen
import personal.secminhr.cavern.mainActivity
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.viewmodel.ArticleContentViewModel
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(preview: ArticlePreview): Screen {

    @ExperimentalMaterialApi
    override val content = @Composable {
        val viewModel: ArticleContentViewModel = viewModel()
        var comments = viewModel.getComments(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }
        ArticleContentView(
            article = viewModel.getArticleContent(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) },
            titleState = topBarTitle,
            preview = preview,
            comments = comments,
            onCommentSend = {
                comments = viewModel.getComments(preview) { mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT) }
            }
        )
    }
    override val topBarIcons: List<@Composable () -> Unit> = super.sameAppBarIconAs(articleScreen)
    override val topBarIconActions: List<() -> Unit> = super.sameAppBarIconActionAs(articleScreen)
    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")
    override val shouldShowBackButton = true

}