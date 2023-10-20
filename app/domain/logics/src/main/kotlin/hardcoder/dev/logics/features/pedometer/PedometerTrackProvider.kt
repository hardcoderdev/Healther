package hardcoder.dev.logics.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.pedometer.PedometerTrackDao
import hardcoder.dev.database.entities.features.pedometer.PedometerTrack
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerTrackProvider(
    private val pedometerTrackDao: PedometerTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun providePedometerTracksByRange(range: ClosedRange<Instant>) = pedometerTrackDao
        .providePedometerTracksByRange(
            range.start,
            range.endInclusive,
            range.start,
            range.endInclusive,
        )
        .mapItems { it.toEntity() }
        .flowOn(dispatchers.io)
}

private fun PedometerTrack.toEntity() = PedometerTrackEntity(
    id = id,
    stepsCount = stepsCount,
    range = startTime..endTime,
)