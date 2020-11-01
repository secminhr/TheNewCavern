package personal.secminhr.cavern.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import personal.secminhr.cavern.ui.style.CavernTheme

@Composable
fun MainActivityView(screen: Screen, addBackButton: Boolean = false, backAction: () -> Unit = {}, history: ScreenStack) {

    CavernTheme {
        Column {
            AppBar(icon = screen.topBarIcon, showBackButton = addBackButton,
                   backAction = backAction, iconAction = { screen.topBarIconAction(history) })
            Crossfade(current = screen) {
                it.content()
            }
        }
    }
}

//@Preview(showBackground = true, name = "ArticleScreen")
//@Composable
//fun DefaultPreview() {
//    MainActivityView(MutableLiveData(articleScreen), MutableLiveData(false))
//}

