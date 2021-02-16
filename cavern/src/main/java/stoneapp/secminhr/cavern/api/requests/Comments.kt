package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.Comment
import stoneapp.secminhr.cavern.getJsonArrayFromKey
import stoneapp.secminhr.cavern.getJsonObjectFromKey
import stoneapp.secminhr.cavern.getStringFromKey
import java.net.URL

class Comments(val id: Int): Result<List<Comment>> {

    override suspend fun get(): List<Comment> = withContext(Dispatchers.IO) {
        val json = runCatching {
            val url = URL("${Cavern.host}/ajax/comment.php?pid=$id")
            url.openStream().inputAsJson()
        }.getOrThrow()

        val arr = json.getJsonArrayFromKey("comments_key")
        val comments = mutableListOf<Comment>()
        if(arr.size() > 0) {
            val nicknames = json.getJsonObjectFromKey("names_key")
            val hashes = json.getJsonObjectFromKey("hash_key")

            for (i in 0 until arr.size()) {
                val comment = arr[i].asJsonObject
                val id = comment.getStringFromKey("id_key").toInt()
                val username = comment.getStringFromKey("username_key")
                val content = comment.getStringFromKey("markdown_key")
                val nickname = nicknames[username].asString
                val hash = hashes[username].asString
                comments += Comment(id, username,
                    nickname,
                    content,
                    "https://www.gravatar.com/avatar/$hash?d=https%3A%2F%2Ftocas-ui.com%2Fassets%2Fimg%2F5e5e3a6.png&s=500")
            }

        }

        comments
    }

}