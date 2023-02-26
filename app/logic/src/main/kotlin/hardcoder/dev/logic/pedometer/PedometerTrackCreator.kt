package hardcoder.dev.logic.pedometer

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.extensions.toMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class PedometerTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun upsertPedometerTrack(
        id: Long,
        range: ClosedRange<LocalDateTime>,
        stepsCount: Int
    ) = withContext(dispatcher) {
        appDatabase.pedometerTrackQueries.upsert(
            id = id,
            stepsCount = stepsCount,
            startTime = range.start.toMillis(),
            endTime = range.endInclusive.toMillis()
        )
    }
}