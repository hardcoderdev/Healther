package hardcoder.dev.controller.request

sealed class RequestState {
    object NotExecuted : RequestState()
    object Executing : RequestState()
    object Executed : RequestState()
}