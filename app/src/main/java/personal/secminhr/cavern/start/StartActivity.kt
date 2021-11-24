package personal.secminhr.cavern.start

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import personal.secminhr.cavern.main.ui.style.logo_green
import personal.secminhr.cavern.main.ui.style.logo_orange
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.*
import kotlin.concurrent.thread

class StartActivity : AppCompatActivity() {

    private var showUpdateDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(applicationContext), CookiePolicy.ACCEPT_ALL))
        }

        var degree by mutableStateOf(45f)
        lifecycleScope.launchWhenStarted {
//            while (true) {
//                delay(500)
//                degree = if (degree + 45f >= 315f) {
//                    45f
//                } else {
//                    degree + 45f
//                }
//            }
        }
        setContent {
            val rotate by animateFloatAsState(targetValue = degree)
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .zIndex(5f)) {
                    rotate(rotate) {
                        val rectSizeRatioToWidth = 8.8f
                        drawRect(
                            color = logo_orange,
                            topLeft = Offset(
                                x = size.width / 2 - size.width / (2 * rectSizeRatioToWidth),
                                y = size.height / 2 - size.width / (2 * rectSizeRatioToWidth)
                            ),
                            size = Size(size.width / rectSizeRatioToWidth, size.width / rectSizeRatioToWidth),
                            style = Stroke(
                                width = 19.7f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    inset(size.width / 9) {
                        drawLine(
                            color = logo_green,
                            start = Offset(x = 0f, y = size.height / 2f),
                            end = Offset(x = size.width, y = size.height / 2f),
                            strokeWidth = 19.7f,
                            cap = StrokeCap.Round
                        )
                        rotate(90f) {
                            drawLine(
                                color = logo_green,
                                start = Offset(x = 0f, y = size.height / 2f),
                                end = Offset(x = size.width, y = size.height / 2f),
                                strokeWidth = 19.7f,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                }

//            IconToggleButton(modifier = Modifier.zIndex(10f), checked = checked, onCheckedChange = {
//                checked = it
//            }) {
//                if (checked) {
//                    Icon(Icons.Default.Send, "")
//                } else {
//                    Icon(Icons.Outlined.Send, "")
//                }
//
//            }
//            Box(modifier = Modifier.fillMaxSize()) {
//                Image(
//                    painter = painterResource(id = R.drawable.logo_img),
//                    null,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(32.dp)
//                        .fillMaxWidth(),
//                    contentScale = ContentScale.FillWidth
//                )
//            }

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
//            startActivity(Intent(this@StartActivity, MainActivity::class.java))
//            finish()
        }
    }

    @Serializable
    private data class AppMeta(val newestVersion: Int)
}