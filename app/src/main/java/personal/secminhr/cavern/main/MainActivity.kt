package personal.secminhr.cavern.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import personal.secminhr.cavern.main.ui.views.AppBar
import personal.secminhr.cavern.main.ui.views.MainActivityView
import personal.secminhr.cavern.main.ui.views.ScreenStack
import personal.secminhr.cavern.main.ui.views.articleContent.ArticleContentScreen
import personal.secminhr.cavern.main.ui.views.articles.ArticleScreen
import personal.secminhr.cavern.main.ui.views.editor.EditorScreen
import personal.secminhr.cavern.main.ui.views.login.LoginScreen
import personal.secminhr.cavern.main.viewmodel.SessionUserViewModel
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            viewModels<SessionUserViewModel>().value.login {
                currentAccount = it
            }
        } catch (e: SessionExpiredError) {
            //left empty
        }

        setContent {
            Column {
                AppBar(screen = screenHistory.currentScreen, backAction = ::onBackPressed)
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

    fun showToast(message: String, duration: Int) {
        runOnUiThread {
            Toast.makeText(this, message, duration).show()
        }
    }

    companion object {
        var currentAccount: Account? by mutableStateOf(null)
        val hasCurrentUser get() = currentAccount != null

        val articleScreen = ArticleScreen()
        fun articleContentScreen(preview: ArticlePreview) = ArticleContentScreen(preview)
        fun editorScreen(title: String? = null, content: String? = null, id: Int? = null) = EditorScreen(title, content, id)
        val loginScreen = LoginScreen()

        val screenHistory = ScreenStack(articleScreen)
    }
}

