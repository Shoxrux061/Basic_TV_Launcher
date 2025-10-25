package uz.shoxrux.app.presentation.screens.main

import uz.shoxrux.app.presentation.base.UiEffect

sealed class MainScreenEffect() : UiEffect {
    object ShowSetLauncherDialog : MainScreenEffect()
    object OpenLauncherChooser : MainScreenEffect()
    data class OpenApp(val packageName: String) : MainScreenEffect()
}