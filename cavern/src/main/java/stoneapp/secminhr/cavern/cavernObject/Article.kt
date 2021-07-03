package stoneapp.secminhr.cavern.cavernObject

import java.io.Serializable

data class Article(val id: Int, val title: String, val authorNickname: String,
                   val authorUsername: String, val isLiked: Boolean, val content: String): Serializable {

    constructor(preview: ArticlePreview, content: String):
            this(preview.id, preview.title, preview.author, preview.authorUsername, preview.liked, content)

    companion object {
        val empty = Article(-1, "","","",false, "")
    }
}