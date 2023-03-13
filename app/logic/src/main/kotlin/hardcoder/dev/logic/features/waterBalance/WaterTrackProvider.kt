package hardcoder.dev.logic.features.waterBalance

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.waterTracking.WaterTrack as WaterTrackEntity

class WaterTrackProvider(
    private val appDatabase: AppDatabase,
    private val drinkTypeIdMapper: DrinkTypeIdMapper
) {

    fun provideWaterTracksByDayRange(dayRange: LongRange) = appDatabase.waterTrackQueries
        .selectWaterTracksByDayRange(dayRange.first, dayRange.last)
        .asFlow()
        .map {
            it.executeAsList().map { waterTrackDatabase ->
                waterTrackDatabase.toEntity()
            }
        }

    fun provideWaterTrackById(id: Int) = appDatabase.waterTrackQueries
        .selectWaterTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun WaterTrack.toEntity() = WaterTrackEntity(
        id, date, millilitersCount, drinkTypeIdMapper.mapToDrinkType(drinkTypeId)
    )
}