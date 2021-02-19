package personal.secminhr.cavern

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.FragmentActivity
import personal.secminhr.cavern.ui.views.AppBar
import personal.secminhr.cavern.ui.views.MainActivityView
import personal.secminhr.cavern.ui.views.ScreenStack
import personal.secminhr.cavern.ui.views.articleContent.ArticleContentScreen
import personal.secminhr.cavern.ui.views.articles.ArticleScreen
import personal.secminhr.cavern.ui.views.editor.EditorScreen
import personal.secminhr.cavern.ui.views.login.LoginScreen
import personal.secminhr.cavern.viewmodel.SessionUserViewModel
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

lateinit var mainActivity: MainActivity
class MainActivity : FragmentActivity() {

    init {
        mainActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenHistory.changeScreen(articleScreen)

        try {
            viewModels<SessionUserViewModel>().value.login {
                currentAccount = it
            }
        } catch (e: SessionExpiredError) {
            //left empty
        }

        setContent {
            Column {
                AppBar(icons = screenHistory.currentScreen.value!!.topBarIcons,
                        title = screenHistory.currentScreen.value!!.topBarTitle,
                        showBackButton = screenHistory.currentScreen.value!!.shouldShowBackButton,
                        backAction = ::onBackPressed,
                        iconActions = screenHistory.currentScreen.value!!.topBarIconActions)
                MainActivityView(screenHistory.currentScreen.value!!)
            }
        }
    }

    override fun onBackPressed() {
        if (screenHistory.currentScreen.value is ArticleScreen) {
            super.onBackPressed() //exit
        } else {
            screenHistory.currentScreen.value?.backToPreviousScreen()
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
        val screenHistory = ScreenStack()

        val articleScreen = ArticleScreen()
        fun articleContentScreen(preview: ArticlePreview) = ArticleContentScreen(preview)
        fun editorScreen(title: String? = null, content: String? = null, id: Int? = null) = EditorScreen(title, content, id)
        val loginScreen = LoginScreen()
    }
}

