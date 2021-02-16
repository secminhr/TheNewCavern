package personal.secminhr.cavern.ui.preview

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import stoneapp.secminhr.cavern.cavernObject.Article
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import java.util.*

//article preview data
val articlePreviewNormalDislike = ArticlePreview("title1", "author1", "username1", Date(), ObservableInt(5), 10, false)
val articlePreviewNormalLike = ArticlePreview("title1", "author1", "username1", Date(), ObservableInt(5), 10, true)
val articlePreviewLongText = ArticlePreview("title1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "author1", "username1", Date(), ObservableInt(5), 10, false)

//screen preview data
//val mockScreen = object:Screen {
//    override val content = @Composable {
//        Text("Content", modifier = Modifier.fillMaxSize())
//    }
//    override val topBarIcon = @Composable {
//        Icon(Icons.Default.AccountCircle, null)
//    }
//    override val topBarIconAction = {}
//    override val shouldShowBackButton = true
//}

//article content preview data
val articleContentDislike = MutableLiveData(Article(813, "Title1", "Sec", "secminhr", false, ""))