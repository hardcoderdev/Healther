package hardcoder.dev.logic.pedometer

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.entities.pedometer.PedometerTrack as PedometerTrackEntity
import kotlinx.coroutines.flow.map

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

    fun providePedometerTrackById(id: Int) = appDatabase.pedometerTrackQueries
        .selectPedometerTrackById(id.toLong())
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id.toInt(), stepsCount, startTime..endTime
    )
}