package hardcoder.dev.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ToggleController(
    private val coroutineScope: CoroutineScope,
    private val toggle: suspend () -> Unit,
    isActiveFlow: Flow<Boolean>,
) : StateController<ToggleController.State> {

    override val state = isActiveFlow.map(::State).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(false),
    )

    fun toggle() {
        coroutineScope.launch {
            toggle.invoke()
        }
    }

    data class State(
        val isActive: Boolean,
    )
}