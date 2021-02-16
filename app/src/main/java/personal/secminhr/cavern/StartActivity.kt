package personal.secminhr.cavern

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stoneapp.secminhr.cavern.api.Cavern

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.logo_img), null,
                    modifier = Modifier.align(Alignment.Center).padding(32.dp).fillMaxWidth(),
                    contentScale = ContentScale.FillWidth)
            }
        }

        lifecycleScope.launchWhenStarted {
            Cavern.initialize(application)
            fetchMappingJson()
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
        }
    }

    private suspend fun fetchMappingJson() = withContext(Dispatchers.IO) {

    }
}