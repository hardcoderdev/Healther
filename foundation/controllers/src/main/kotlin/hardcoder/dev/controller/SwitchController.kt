package hardcoder.dev.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SwitchController(
    coroutineScope: CoroutineScope,
    initialValue: Boolean = false,
) : StateController<SwitchController.State> {

    private var _state = MutableStateFlow(false)
    override val state: StateFlow<State> = _state.map {
        State(isEnabled = it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(isEnabled = initialValue),
    )

    fun toggle() {
         _state.value = !state.value.isEnabled
    }

    fun isActive() = state.value.isEnabled

    data class State(val isEnabled: Boolean)
}