package uz.shoxrux.app.presentation.screens.main

import uz.shoxrux.app.domain.model.AppModelUi
import uz.shoxrux.app.presentation.base.UiIntent

sealed class MainScreenIntent : UiIntent {
    object LoadApps : MainScreenIntent()
    object AskSetLauncher : MainScreenIntent()
    object ConfirmSetLauncher : MainScreenIntent()
    data class OpenApp(val packageName: String) : MainScreenIntent()

    object CloseDialog : MainScreenIntent()
}
