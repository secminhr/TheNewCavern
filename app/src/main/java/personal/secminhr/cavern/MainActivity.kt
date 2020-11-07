package personal.secminhr.cavern

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.FragmentActivity
import personal.secminhr.cavern.ui.views.MainActivityView
import personal.secminhr.cavern.ui.views.ScreenStack
import personal.secminhr.cavern.ui.views.articleContent.ArticleContentScreen
import personal.secminhr.cavern.ui.views.articles.ArticleScreen
import personal.secminhr.cavern.viewmodel.SessionUserViewModel
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernError.SessionExpiredError
import stoneapp.secminhr.cavern.cavernObject.Account

class MainActivity : FragmentActivity() {

    private var shouldShowBackButton by mutableStateOf(false)

    private val articleScreen: ArticleScreen by lazy {
        ArticleScreen {
            screenHistory.changeScreen(ArticleContentScreen(it))
            shouldShowBackButton = true
        }
    }
    private val screenHistory by lazy {
        ScreenStack(articleScreen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Cavern.initialize(application)

        try {
            viewModels<SessionUserViewModel>().value.login {
                currentAccount = it.account
            }
        } catch (e: SessionExpiredError) {
            //left empty
        }

        setContent {
            MainActivityView(screenHistory.currentScreen.value, shouldShowBackButton,
                    backAction = ::onBackPressed, screenHistory)
        }
    }

    override fun onBackPressed() {
        if (screenHistory.currentScreen.value is ArticleScreen) {
            super.onBackPressed() //exit
        } else {
            screenHistory.back()
            shouldShowBackButton = false
        }
    }

    companion object {
        var currentAccount: Account? by mutableStateOf(null)
    }
}

