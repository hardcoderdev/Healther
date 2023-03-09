package hardcoder.dev.logic.pedometer

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerTrackProvider(private val appDatabase: AppDatabase) {

    fun providePedometerTracksByRange(range: LongRange) = appDatabase.pedometerTrackQueries
        .selectPedometerTracksByRange(
            range.first,
            range.last,
            range.first,
            range.last
        )
        .asFlow()
        .map {
            it.executeAsList().map { pedometerDatabase ->
                pedometerDatabase.toEntity()
            }
        }

    private fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id.toInt(), stepsCount, startTime..endTime
    )
}