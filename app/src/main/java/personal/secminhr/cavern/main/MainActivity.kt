package personal.secminhr.cavern.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.fragment.app.FragmentActivity
import personal.secminhr.cavern.main.ui.views.AppBar
import personal.secminhr.cavern.main.ui.views.MainActivityView
import personal.secminhr.cavern.main.ui.views.ScreenStack
import personal.secminhr.cavern.main.ui.views.articleContent.ArticleContentScreen
import personal.secminhr.cavern.main.ui.views.articles.ArticleScreen
import personal.secminhr.cavern.main.ui.views.backToPreviousScreen
import personal.secminhr.cavern.main.ui.views.editor.EditorScreen
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            viewModels<CurrentUserViewModel>().value.sessionLogin()
        } catch (e: SessionExpiredError) {
            //left empty
        }

        setContent {
            Column {
                AppBar(screenHistory.currentScreen.barScope, screenHistory.currentScreen.shouldShowBackButton, backAction = ::onBackPressed)
                MainActivityView(screenHistory.currentScreen)
            }
        }
    }

    override fun onBackPressed() {
        if (screenHistory.currentScreen is ArticleScreen) {
            super.onBackPressed() //exit
        } else {
            screenHistory.currentScreen.backToPreviousScreen()
        }
    }

    companion object {
        fun articleContentScreen(preview: ArticlePreview) = ArticleContentScreen(preview)
        fun editorScreen(title: String? = null, content: String? = null, id: Int? = null) = EditorScreen(title, content, id)

        val screenHistory = ScreenStack(ArticleScreen)
    }
}

