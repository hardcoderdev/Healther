package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerTrackProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun providePedometerTracksByRange(range: ClosedRange<Instant>) = appDatabase.pedometerTrackQueries
        .providePedometerTracksByRange(
            range.start,
            range.endInclusive,
            range.start,
            range.endInclusive
        )
        .asFlowOfList(dispatchers.io) { pedometerDatabase ->
            pedometerDatabase.toEntity()
        }

    private fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id, stepsCount, startTime..endTime
    )
}