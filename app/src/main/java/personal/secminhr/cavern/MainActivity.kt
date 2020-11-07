package personal.secminhr.cavern

import android.os.Bundle
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
import personal.secminhr.cavern.ui.views.login.LoginScreen
import personal.secminhr.cavern.viewmodel.SessionUserViewModel
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.Account
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Cavern.initialize(application)

        screenHistory.changeScreen(articleScreen)

        try {
            viewModels<SessionUserViewModel>().value.login {
                currentAccount = it.account
            }
        } catch (e: SessionExpiredError) {
            //left empty
        }

        setContent {
            Column {
                AppBar(icon = screenHistory.currentScreen.value!!.topBarIcon,
                        showBackButton = screenHistory.currentScreen.value!!.shouldShowBackButton,
                        backAction = ::onBackPressed,
                        iconAction = screenHistory.currentScreen.value!!.topBarIconAction)
                MainActivityView(screenHistory.currentScreen.value!!)
            }
        }
    }

    override fun onBackPressed() {
        if (screenHistory.currentScreen.value is ArticleScreen) {
            super.onBackPressed() //exit
        } else {
            screenHistory.back()
        }
    }

    companion object {
        var currentAccount: Account? by mutableStateOf(null)
        val screenHistory = ScreenStack()

        val articleScreen = ArticleScreen()
        fun articleContentScreen(preview: ArticlePreview) = ArticleContentScreen(preview)
        val loginScreen = LoginScreen()
    }
}

