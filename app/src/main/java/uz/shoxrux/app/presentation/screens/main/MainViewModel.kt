package uz.shoxrux.app.presentation.screens.main

import dagger.hilt.android.lifecycle.HiltViewModel
import uz.shoxrux.app.domain.use_case.GetAppListUseCase
import uz.shoxrux.app.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: GetAppListUseCase,
    reducer: MainScreenReducer
) : BaseViewModel<MainScreenIntent, MainScreenState, MainScreenEffect>(
    initialState = MainScreenState(),
    reducer = reducer
) {

    override fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.LoadApps -> loadApps()
            is MainScreenIntent.OpenApp -> openApp(intent.packageName)
            is MainScreenIntent.AskSetLauncher -> sendEffect { MainScreenEffect.ShowSetLauncherDialog }
            is MainScreenIntent.ConfirmSetLauncher -> sendEffect { MainScreenEffect.OpenLauncherChooser }
            else -> Unit
        }
    }

    private fun loadApps() {
        launchSafely(
            onError = { error ->
                updateState { copy(isLoading = false, error = error.message ?: "Ошибка загрузки") }
            }
        ) {
            updateState { copy(isLoading = true) }

            val apps = useCase()
            updateState { copy(isLoading = false, appList = apps) }
        }
    }

    private fun openApp(packageName: String) {
        sendEffect { MainScreenEffect.OpenApp(packageName) }
    }
}