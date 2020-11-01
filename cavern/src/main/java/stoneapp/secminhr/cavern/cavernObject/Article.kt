package stoneapp.secminhr.cavern.cavernObject

import java.io.Serializable

data class Article(val id: Int, val title: String, val authorNickname: String,
                   val authorUsername: String, val isLiked: Boolean, val content: String): Serializable {
    companion object {
        val empty = Article(-1, "","","",false, "")
    }
}