package personal.secminhr.cavern.start

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import personal.secminhr.cavern.commonUI.AppLogo
import personal.secminhr.cavern.main.MainActivity
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URL
import kotlin.concurrent.thread

class StartActivity : AppCompatActivity() {

    private var showUpdateDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(applicationContext), CookiePolicy.ACCEPT_ALL))
        }

        setContent {
            AppLogo()
            if (showUpdateDialog) {
                RequireUpdateDialog(
                    update = this::startActivity,
                    dismiss = this::finish
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            checkUpdate(this@StartActivity)
        }
    }
    private suspend fun checkUpdate(context: Context) = withContext(Dispatchers.IO) {
        val url = URL("https://cavern-8e04d.firebaseio.com/appMeta.json")
        val appMeta: AppMeta = Json { ignoreUnknownKeys = true }.decodeFromString(url.openStream().reader().readText())
        val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val currentVersion: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
        if (appMeta.newestVersion > currentVersion) {
            showUpdateDialog = true
        } else {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()
        }
    }

    @Serializable
    private data class AppMeta(val newestVersion: Int)
}