package stoneapp.secminhr.cavern.cavernObject

import androidx.databinding.ObservableInt
import java.io.Serializable
import java.util.*


data class ArticlePreview(val title: String, val author: String, val authorUsername: String, val date: Date,
                          var upvote: ObservableInt, val id: Int, var liked: Boolean): Serializable {
    companion object {
        val empty = ArticlePreview("", "", "", Date(), ObservableInt(0), -1, false)
    }
}
