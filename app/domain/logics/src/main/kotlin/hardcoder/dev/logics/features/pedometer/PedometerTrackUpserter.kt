package hardcoder.dev.logics.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.dao.features.pedometer.PedometerTrackDao
import hardcoder.dev.database.entities.features.pedometer.PedometerTrack
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class PedometerTrackUpserter(
    private val pedometerTrackDao: PedometerTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun upsert(
        id: Int,
        range: ClosedRange<LocalDateTime>,
        stepsCount: Int,
    ) = withContext(dispatchers.io) {
        pedometerTrackDao.upsert(
            PedometerTrack(
                id = id,
                stepsCount = stepsCount,
                startTime = range.start.toInstant(TimeZone.currentSystemDefault()),
                endTime = range.endInclusive.toInstant(TimeZone.currentSystemDefault()),
            ),
        )
    }
}