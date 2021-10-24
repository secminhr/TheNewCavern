package personal.secminhr.cavern.start

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun RequireUpdateDialog(update: (Intent) -> Unit, dismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Update Available") },
        text = {
            Column {
                Text("There is a newer version of Cavern App")
                Text("This update is required. The app will close if you choose not to update.")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/secminhr/TheNewCavern/releases/latest")
                    update(intent)
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text("No")
            }
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}