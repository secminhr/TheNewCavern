package personal.secminhr.cavern.main.ui.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import personal.secminhr.cavern.commonUI.AppLogo
import personal.secminhr.cavern.main.viewmodel.CurrentUserViewModel
import personal.secminhr.cavern.main.viewmodel.UserInfoViewModel
import stoneapp.secminhr.cavern.cavernObject.Account

@Composable
fun UserView(username: String, showSnackbar: (String) -> Unit) {
    val infoViewModel = viewModel<UserInfoViewModel>()
    val user = remember(username) {
        infoViewModel.getAuthorInfo(username) {
            showSnackbar(it.message!!)
        }
    }

    if (user.value == null) {
        Column(Modifier.height(200.dp)) {
            AppLogo(loading = true)
        }
    } else {
        UserView(user = user.value!!)
    }
}

@Composable
fun UserView(user: Account) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Image(
            painter = rememberImagePainter(user.avatarLink),
            contentDescription = null,
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .heightIn(max = 150.dp)
                .padding(0.dp, 0.dp, 0.dp, 8.dp)
        )
        DataSection(user)
        val viewModel = viewModel<CurrentUserViewModel>()
        if (user == viewModel.currentUser) {
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun DataSection(user: Account) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            Text(
                "基本資料",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("使用者名稱", modifier = Modifier.weight(0.5f, true))
            Text(user.username, modifier = Modifier.weight(0.5f, true))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("暱稱", modifier = Modifier.weight(0.5f, true))
            Text(user.nickname, modifier = Modifier.weight(0.5f, true))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("權限", modifier = Modifier.weight(0.5f, true))
            Text(user.role.name, modifier = Modifier.weight(0.5f, true))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            Text(
                "統計",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("文章數", modifier = Modifier.weight(0.5f, true))
            Text(user.postCount.toString(), modifier = Modifier.weight(0.5f, true))
        }
    }
}