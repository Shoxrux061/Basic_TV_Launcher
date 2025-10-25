package uz.shoxrux.app.presentation.screens.main

import uz.shoxrux.app.domain.model.AppModelUi
import uz.shoxrux.app.presentation.base.UiState

data class MainScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val appList: List<AppModelUi> = emptyList(),
    val showSetLauncherDialog: Boolean = false
) : UiState