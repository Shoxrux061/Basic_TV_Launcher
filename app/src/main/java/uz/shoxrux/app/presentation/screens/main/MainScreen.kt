package uz.shoxrux.app.presentation.screens.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.error) {
        Log.d("`TAGError`", "MainScreen: ${state.error}")
    }



    LaunchedEffect(Unit) {
        viewModel.onIntent(MainScreenIntent.LoadApps)
        viewModel.onIntent(MainScreenIntent.AskSetLauncher)

        viewModel.effect.collect { e ->
            when (e) {
                is MainScreenEffect.OpenApp -> {
                    try {
                        val launchIntent =
                            context.packageManager.getLaunchIntentForPackage(e.packageName)
                        if (launchIntent != null) {
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(launchIntent)
                        } else if (e.packageName == "com.android.tv.settings") {

                            val settingsIntent =
                                Intent(android.provider.Settings.ACTION_SETTINGS).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            context.startActivity(settingsIntent)
                        } else {
                            Log.w("TAGLauncher", "App ${e.packageName} has no launch intent")
                            Toast.makeText(
                                context,
                                "Невозможно запустить ${e.packageName}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (ex: Exception) {
                        Log.e(
                            "TAGLauncher",
                            "Ошибка при запуске ${e.packageName}: ${ex.message}",
                            ex
                        )
                        Toast.makeText(context, "Ошибка при запуске приложения", Toast.LENGTH_SHORT)
                            .show()
                    }
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
            .padding(horizontal = 50.dp),
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
        title = {
            Text(
                color = Color.Black,
                text = "Сделать приложение лаунчером?"
            )
        },
        text = {
            Text(
                text = "Это заменит стандартный лаунчер Android TV.",
                color = Color.Black,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) { Text("Да") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Нет") } }
    )
}