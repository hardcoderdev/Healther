package hardcoder.dev.logic.providers

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
import hardcoder.dev.entities.DrinkType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class WaterTrackProvider(private val appDatabase: AppDatabase) {

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

    fun getAllDrinkTypes(): Flow<List<DrinkType>> {
        return flowOf(
            listOf(
                DrinkType.WATER,
                DrinkType.TEA,
                DrinkType.COFFEE,
                DrinkType.BEER,
                DrinkType.JUICE,
                DrinkType.SODA,
                DrinkType.SOUP,
                DrinkType.MILK,
            )
        )
    }

    private fun WaterTrack.toEntity() = hardcoder.dev.entities.WaterTrack(
        id, date, millilitersCount, drinkType
    )
}