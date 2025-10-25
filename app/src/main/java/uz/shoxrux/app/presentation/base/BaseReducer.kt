package uz.shoxrux.app.presentation.base

interface BaseReducer<I : UiIntent, S : UiState> {
    fun reduce(intent: I, currentState: S): S
}