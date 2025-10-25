package uz.shoxrux.app.presentation.screens.main

import uz.shoxrux.app.presentation.base.BaseReducer

class MainScreenReducer : BaseReducer<MainScreenIntent, MainScreenState> {

    override fun reduce(
        intent: MainScreenIntent,
        currentState: MainScreenState
    ): MainScreenState {
        return when (intent) {
            is MainScreenIntent.LoadApps -> currentState.copy(isLoading = true)
            is MainScreenIntent.AskSetLauncher -> currentState.copy(showSetLauncherDialog = true)
            is MainScreenIntent.ConfirmSetLauncher -> currentState.copy(showSetLauncherDialog = false)
            is MainScreenIntent.CloseDialog -> currentState.copy(showSetLauncherDialog = false)
            else -> currentState
        }
    }
}
