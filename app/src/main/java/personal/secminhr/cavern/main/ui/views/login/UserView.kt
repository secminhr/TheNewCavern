package personal.secminhr.cavern.main.ui.views.login

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import personal.secminhr.cavern.R
import personal.secminhr.cavern.main.MainActivity
import personal.secminhr.cavern.main.mainActivity
import personal.secminhr.cavern.main.viewmodel.LogoutViewModel
import personal.secminhr.cavern.main.viewmodel.UserInfoViewModel

@Composable
fun UserView(username: State<String>) {

    val infoViewModel = viewModel<UserInfoViewModel>()
    val user = remember(username.value) {
        infoViewModel.getAuthorInfo(username.value) {
            mainActivity.showToast(it.message!!, Toast.LENGTH_SHORT)
        }
    }

    val avatarImageView = ImageView(LocalContext.current)
    val logoutViewModel: LogoutViewModel = viewModel()

    if (user.value == null) {
        CircularProgressIndicator()
    } else {
        Glide.with(LocalContext.current)
            .asDrawable()
            .load(user.value!!.avatarLink)
            .placeholder(R.drawable.logo_img)
            .into(avatarImageView)

        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            AndroidView(
                { avatarImageView },
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(0.dp, 0.dp, 0.dp, 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).background(Color.LightGray).padding(8.dp)) {
                Text(
                    "基本資料",
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.LightGray)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)
            ) {
                Text("使用者名稱", modifier = Modifier.weight(0.5f, true))
                Text(username.value, modifier = Modifier.weight(0.5f, true))
            }

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)
            ) {
                Text("暱稱", modifier = Modifier.weight(0.5f, true))
                Text(user.value!!.nickname, modifier = Modifier.weight(0.5f, true))
            }

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)
            ) {
                Text("權限", modifier = Modifier.weight(0.5f, true))
                Text(user.value!!.role.name, modifier = Modifier.weight(0.5f, true))
            }

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).background(Color.LightGray).padding(8.dp)) {
                Text(
                    "統計",
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.LightGray)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(8.dp)
            ) {
                Text("文章數", modifier = Modifier.weight(0.5f, true))
                Text(user.value!!.postCount.toString(), modifier = Modifier.weight(0.5f, true))
            }

            if (user.value == MainActivity.currentAccount) {
                OutlinedButton(
                    onClick = {
                        logoutViewModel.logout {
                            MainActivity.currentAccount = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}