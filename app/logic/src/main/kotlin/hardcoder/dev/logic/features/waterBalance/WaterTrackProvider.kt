package hardcoder.dev.logic.features.waterBalance

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.waterTracking.WaterTrack as WaterTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackProvider(
    private val appDatabase: AppDatabase,
    private val drinkTypeProvider: DrinkTypeProvider
) {

    fun provideWaterTracksByDayRange(dayRange: LongRange) = appDatabase.waterTrackQueries
        .provideWaterTracksByDayRange(dayRange.first, dayRange.last)
        .asFlow()
        .map {
            it.executeAsList()
        }.flatMapLatest { waterTracksList ->
            if (waterTracksList.isEmpty()) flowOf(emptyList())
            else combine(
                waterTracksList.map { waterTrack ->
                    provideDrinkTypeById(waterTrack)
                }
            ) {
                it.toList()
            }
        }

    fun provideWaterTrackById(id: Int) = appDatabase.waterTrackQueries
        .provideWaterTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()
        }.flatMapLatest {
            if (it == null) {
                flowOf(null)
            } else {
                provideDrinkTypeById(it)
            }
        }

    private fun provideDrinkTypeById(waterTrack: WaterTrack): Flow<WaterTrackEntity> {
        return drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
            waterTrack.toEntity(drinkType!!)
        }
    }

    private fun WaterTrack.toEntity(drinkType: DrinkType) = WaterTrackEntity(
        id, date, millilitersCount, drinkType
    )
}