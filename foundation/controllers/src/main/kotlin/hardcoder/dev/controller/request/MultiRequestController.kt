package hardcoder.dev.controller.request

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MultiRequestController(
    coroutineScope: CoroutineScope,
    request: suspend () -> Unit,
    isAllowedFlow: Flow<Boolean> = flowOf(true),
) : SingleRequestController(
    coroutineScope = coroutineScope,
    request = request,
    isAllowedFlow = isAllowedFlow,
) {
    override fun request() {
        super.request()
        requestState.value = RequestState.NotExecuted
    }
}