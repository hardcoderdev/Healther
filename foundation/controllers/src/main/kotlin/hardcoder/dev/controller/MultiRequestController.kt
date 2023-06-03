package hardcoder.dev.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MultiRequestController(
    private val coroutineScope: CoroutineScope,
    private val request: suspend () -> Unit,
    isAllowedFlow: Flow<Boolean> = flowOf(true)
) : StateController<MultiRequestController.State> {

    private val requestState = MutableStateFlow<RequestState>(RequestState.NotExecuted)
    override val state = combine(
        requestState,
        isAllowedFlow
    ) { requestState, isAllowed ->
        State(
            requestState = requestState,
            isRequestAllowed = isAllowed && requestState is RequestState.NotExecuted
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isRequestAllowed = false,
            requestState = RequestState.NotExecuted
        )
    )

    fun request() {
        coroutineScope.launch {
            try {
                requestState.value = RequestState.Executing
                require(state.value.isRequestAllowed)
                request.invoke()
                requestState.value = RequestState.Executed
                requestState.value = RequestState.NotExecuted
            } catch (e: Exception) {
                requestState.value = RequestState.NotExecuted
            }
        }
    }

    data class State(
        val isRequestAllowed: Boolean,
        val requestState: RequestState
    )

    sealed class RequestState {
        object NotExecuted : RequestState()
        object Executing : RequestState()
        object Executed : RequestState()
    }
}