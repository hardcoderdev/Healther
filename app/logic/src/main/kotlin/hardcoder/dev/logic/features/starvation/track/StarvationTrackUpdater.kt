package hardcoder.dev.logic.features.starvation.track

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class StarvationTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun interruptStarvation(trackId: Int, duration: Long) = withContext(dispatcher) {
        appDatabase.starvationTrackQueries.interruptStarvation(
            id = trackId,
            interruptedTimeInMillis = System.currentTimeMillis(),
            duration = duration
        )
    }
}