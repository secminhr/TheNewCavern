package personal.secminhr.cavern.ui.views.articleContent

import android.widget.ImageView
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.viewModel
import com.bumptech.glide.Glide
import personal.secminhr.cavern.ArticleContentViewModel
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.ui.views.Screen
import personal.secminhr.cavern.ui.views.ScreenStack
import personal.secminhr.cavern.ui.views.login.LoginScreen
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview

class ArticleContentScreen(preview: ArticlePreview): Screen {

    override val content = @Composable {
        val viewModel: ArticleContentViewModel = viewModel()
        ArticleContentView(article = viewModel.getArticleContent(preview))
    }
    override val topBarIcon = @Composable {
        if (MainActivity.currentAccount != null) {
            val image = MainActivity.currentAccount!!.avatarLink
            val imageView = ImageView(ContextAmbient.current)
            Glide.with(ContextAmbient.current)
                    .asDrawable()
                    .load(image)
                    .into(imageView)
            AndroidView({ imageView }, modifier = Modifier.clip(CircleShape).size(24.dp))

        } else {
            Icon(Icons.Default.AccountCircle)
        }
    }
    override val topBarIconAction: (ScreenStack) -> Unit = {
        it.changeScreen(LoginScreen())
    }

}