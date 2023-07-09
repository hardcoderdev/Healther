package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class PedometerTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun upsertPedometerTrack(
        id: Int,
        range: ClosedRange<LocalDateTime>,
        stepsCount: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.pedometerTrackQueries.upsert(
            id = id,
            stepsCount = stepsCount,
            startTime = range.start.toInstant(TimeZone.currentSystemDefault()),
            endTime = range.endInclusive.toInstant(TimeZone.currentSystemDefault()),
        )
    }
}