package personal.secminhr.cavern.ui.views.login

import android.widget.ImageView
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.viewModel
import com.bumptech.glide.Glide
import personal.secminhr.cavern.MainActivity
import personal.secminhr.cavern.R
import personal.secminhr.cavern.viewmodel.LogoutViewModel
import stoneapp.secminhr.cavern.cavernObject.Account

@Composable
fun UserView(user: Account) {

    val avatarImageView = ImageView(ContextAmbient.current)
    val logoutViewModel: LogoutViewModel = viewModel()

    Glide.with(ContextAmbient.current)
            .asDrawable()
            .load(user.avatarLink)
            .placeholder(R.drawable.logo_img)
            .into(avatarImageView)

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        AndroidView({ avatarImageView }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 8.dp))
        Box(modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(8.dp)) {
            Text("基本資料",
                    modifier = Modifier.fillMaxWidth()
                            .background(Color.LightGray))
        }
        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)) {
            Text("使用者名稱", modifier = Modifier.weight(0.5f, true))
            Text(user.username, modifier = Modifier.weight(0.5f, true))
        }

        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)) {
            Text("暱稱", modifier = Modifier.weight(0.5f, true))
            Text(user.nickname, modifier = Modifier.weight(0.5f, true))
        }

        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)) {
            Text("權限", modifier = Modifier.weight(0.5f, true))
            Text(user.role.name, modifier = Modifier.weight(0.5f, true))
        }

        Box(modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(8.dp)) {
            Text("統計",
                    modifier = Modifier.fillMaxWidth()
                            .background(Color.LightGray))
        }

        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)) {
            Text("文章數", modifier = Modifier.weight(0.5f, true))
            Text(user.postCount.toString(), modifier = Modifier.weight(0.5f, true))
        }

        if (user == MainActivity.currentAccount) {
            OutlinedButton(onClick = {
                logoutViewModel.logout {
                    MainActivity.currentAccount = null
                }
            }, modifier = Modifier.fillMaxWidth().padding(16.dp), contentColor = Color.Red) {
                Text("Logout")
            }
        }
    }
}