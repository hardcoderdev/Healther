package hardcoder.dev.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface BackgroundCoroutineDispatchers {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}