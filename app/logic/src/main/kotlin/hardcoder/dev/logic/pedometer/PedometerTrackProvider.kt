package hardcoder.dev.logic.pedometer

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.entities.pedometer.PedometerTrack as PedometerTrackEntity
import kotlinx.coroutines.flow.map

class PedometerTrackProvider(private val appDatabase: AppDatabase) {

    fun providePedometerTracksByDayRange(dayRange: LongRange) = appDatabase.pedometerTrackQueries
        .selectPedometerTracksByDayRange(dayRange.first, dayRange.last)
        .asFlow()
        .map {
            it.executeAsList().map { pedometerDatabase ->
                pedometerDatabase.toEntity()
            }
        }

    fun providePedometerTrackById(id: Int) = appDatabase.pedometerTrackQueries
        .selectPedometerTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id, stepsCount, caloriesCount, wastedTimeInMinutes, date
    )
}