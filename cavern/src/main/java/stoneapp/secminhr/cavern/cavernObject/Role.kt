package stoneapp.secminhr.cavern.cavernObject

data class Role(val name: String,
                val canPostArticle: Boolean,
                val canLike: Boolean,
                val canComment: Boolean)