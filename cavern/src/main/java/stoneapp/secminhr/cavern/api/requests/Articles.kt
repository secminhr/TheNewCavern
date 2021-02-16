package stoneapp.secminhr.cavern.api.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import stoneapp.secminhr.cavern.api.Cavern
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview
import stoneapp.secminhr.cavern.cavernService.gson
import stoneapp.secminhr.cavern.getJsonArrayFromKey
import java.net.URL

class Articles(private val pageLimit: Int): Result<Flow<Pair<Int, List<ArticlePreview>>>> {

    override suspend fun get(): Flow<Pair<Int, List<ArticlePreview>>> = flow {
        var counter = 1
        while (true) {
            val list = mutableListOf<ArticlePreview>()
            runCatching {
                val url = URL("${Cavern.host}/ajax/posts.php?page=$counter&limit=$pageLimit")
                url.openStream().inputAsJson()
            }.getOrThrow().run {
                list.addAll(gson.fromJson(
                    getJsonArrayFromKey("posts_key"),
                    Array<ArticlePreview>::class.java
                ))
            }

            if (list.size <= 0) {
                counter = 1
                continue
            }

            emit(counter to list)
            counter++
        }
    }.flowOn(Dispatchers.IO)
}