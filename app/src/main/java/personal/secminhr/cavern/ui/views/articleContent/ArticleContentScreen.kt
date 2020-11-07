package personal.secminhr.cavern.ui.views.articleContent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.viewinterop.viewModel
import personal.secminhr.cavern.MainActivity.Companion.articleScreen
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.viewmodel.ArticleContentViewModel
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(preview: ArticlePreview): Screen {

    override val content = @Composable {
        val viewModel: ArticleContentViewModel = viewModel()
        ArticleContentView(article = viewModel.getArticleContent(preview), topBarTitle)
    }
    override val topBarIcon: @Composable () -> Unit = super.sameAppBarIconAs(articleScreen)
    override val topBarIconAction: () -> Unit = super.sameAppBarIconActionAs(articleScreen)
    override val topBarTitle: MutableState<String> = mutableStateOf("Cavern")
    override val shouldShowBackButton = true

}