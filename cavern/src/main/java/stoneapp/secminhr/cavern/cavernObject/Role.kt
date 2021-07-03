package stoneapp.secminhr.cavern.cavernObject

import java.io.Serializable

data class Role(val name: String,
                val canPostArticle: Boolean,
                val canLike: Boolean,
                val canComment: Boolean): Serializable