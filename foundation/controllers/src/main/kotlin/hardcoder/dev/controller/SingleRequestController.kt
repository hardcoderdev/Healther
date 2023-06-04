package hardcoder.dev.controller

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SingleRequestController(
    private val coroutineScope: CoroutineScope,
    private val request: suspend () -> Unit,
    isAllowedFlow: Flow<Boolean> = flowOf(true),
) : StateController<SingleRequestController.State> {

    private val requestState = MutableStateFlow<RequestState>(RequestState.NotExecuted())
    override val state = combine(
        requestState,
        isAllowedFlow
    ) { requestState, isAllowed ->
        State(
            isRequestAllowed = isAllowed && requestState is RequestState.NotExecuted,
            requestState = requestState
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isRequestAllowed = false,
            requestState = RequestState.NotExecuted()
        )
    )

    fun request() {
        coroutineScope.launch {
            try {
                requestState.value = RequestState.Executing()
                require(state.value.isRequestAllowed)
                request.invoke()
                requestState.value = RequestState.Executed()
            } catch (e: Exception) {
                requestState.value = RequestState.NotExecuted()
            }
        }
    }

    data class State(
        val isRequestAllowed: Boolean,
        val requestState: RequestState
    )

    sealed class RequestState {
        class NotExecuted : RequestState()
        class Executing : RequestState()
        class Executed : RequestState()
    }
}