package uz.shoxrux.app.presentation.screens.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.onIntent(MainScreenIntent.LoadApps)
        viewModel.onIntent(MainScreenIntent.AskSetLauncher)

        viewModel.effect.collect { e ->
            when (e) {
                is MainScreenEffect.OpenApp -> {
                    val intent = context.packageManager.getLaunchIntentForPackage(e.packageName)
                    intent?.let { context.startActivity(it) }
                }

                is MainScreenEffect.OpenLauncherChooser -> {
                    Log.d("TAGLauncher", "MainScreen: open chooser")
                    val intent = Intent(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_HOME)
                        addCategory(Intent.CATEGORY_DEFAULT)
                    }
                    val chooser = Intent.createChooser(intent, "Выберите лаунчер по умолчанию")
                    chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(chooser)
                }


                is MainScreenEffect.ShowSetLauncherDialog -> {
                    viewModel.onIntent(MainScreenIntent.AskSetLauncher)
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(state.appList.size) { index ->
                    val app = state.appList[index]
                    AppItem(
                        name = app.name,
                        icon = app.icon,
                        onAppClicked = {
                            viewModel.onIntent(MainScreenIntent.OpenApp(app.packageName))
                        }
                    )
                }
            }
        }

        if (state.showSetLauncherDialog) {
            SetLauncherDialog(
                onConfirm = { viewModel.onIntent(MainScreenIntent.ConfirmSetLauncher) },
                onDismiss = { viewModel.onIntent(MainScreenIntent.CloseDialog) }
            )
        }
    }
}


@Composable
fun AppItem(
    name: String,
    icon: Drawable,
    onAppClicked: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = onAppClicked,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Image(
                modifier = Modifier.size(48.dp),
                painter = rememberDrawablePainter(icon),
                contentDescription = null
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = name
            )
        }
    }
}

@Composable
fun SetLauncherDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Сделать приложение лаунчером?") },
        text = { Text("Это заменит стандартный лаунчер Android TV.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors()
            ) { Text("Да") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Нет") } }
    )
}