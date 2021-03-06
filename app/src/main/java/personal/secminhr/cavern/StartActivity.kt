package personal.secminhr.cavern

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.concurrent.thread

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(applicationContext), CookiePolicy.ACCEPT_ALL))
        }

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.logo_img), null,
                    modifier = Modifier.align(Alignment.Center).padding(32.dp).fillMaxWidth(),
                    contentScale = ContentScale.FillWidth)
            }
        }

        lifecycleScope.launchWhenStarted {
            fetchMappingJson()
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
        }
    }

    private suspend fun fetchMappingJson() = withContext(Dispatchers.IO) {

    }
}