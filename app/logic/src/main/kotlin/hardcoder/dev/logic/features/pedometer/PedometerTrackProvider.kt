package hardcoder.dev.logic.features.pedometer

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.entities.features.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerTrackProvider(private val appDatabase: AppDatabase) {

    fun providePedometerTracksByRange(range: ClosedRange<Instant>) = appDatabase.pedometerTrackQueries
        .providePedometerTracksByRange(
            range.start,
            range.endInclusive,
            range.start,
            range.endInclusive
        )
        .asFlow()
        .map {
            it.executeAsList().map { pedometerDatabase ->
                pedometerDatabase.toEntity()
            }
        }

    private fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id, stepsCount, startTime..endTime
    )
}