package hardcoder.dev.uikit.components.sideEffects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import hardcoder.dev.controller.request.RequestState
import hardcoder.dev.controller.request.SingleRequestController

@Composable
inline fun <reified T : RequestState> LaunchedEffectWhenRequest(
    controller: SingleRequestController,
    crossinline action: suspend () -> Unit,
) {
    val requestState = controller.state.collectAsState().value.requestState
    LaunchedEffect(requestState) {
        if (requestState is T) {
            action()
        }
    }
}

@Composable
fun LaunchedEffectWhenExecuted(
    controller: SingleRequestController,
    action: suspend () -> Unit,
) {
    LaunchedEffectWhenRequest<RequestState.Executed>(
        controller = controller,
        action = action,
    )
}