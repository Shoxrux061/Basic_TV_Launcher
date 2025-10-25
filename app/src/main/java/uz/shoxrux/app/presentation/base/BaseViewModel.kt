package uz.shoxrux.app.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S,
    private val reducer: BaseReducer<I, S>
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    fun onIntent(intent: I) {
        _state.value = reducer.reduce(intent, _state.value)
        handleIntent(intent)
    }

    protected abstract fun handleIntent(intent: I)

    protected fun launchSafely(
        onError: (Throwable) -> Unit = {},
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    protected fun updateState(reducer: S.() -> S) {
        _state.value = _state.value.reducer()
    }

    protected fun sendEffect(builder: () -> E) {
        viewModelScope.launch {
            _effect.emit(builder())
        }
    }
}
