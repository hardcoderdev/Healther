package hardcoder.dev.logic.features.waterBalance.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.entities.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.waterTracking.WaterTrack as WaterTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val drinkTypeProvider: DrinkTypeProvider
) {

    fun provideWaterTrackingStatistic() = appDatabase.waterTrackQueries
        .provideAllWaterTracks()
        .asFlow()
        .map {
            it.executeAsList()
        }.flatMapLatest { waterTracksList ->
            if (waterTracksList.isEmpty()) flowOf(emptyList())
            else combine(
                waterTracksList.map { waterTrack ->
                    provideDrinkTypeById(waterTrack)
                }
            ) { waterTrackArray ->
                val waterTrackList = waterTrackArray.toList()
                val totalMilliliters = waterTrackList.sumOf { it.millilitersCount }
                val favouriteDrinkType = waterTrackList.maxByOrNull { it.drinkType.id }?.drinkType

                listOf(
                    WaterTrackingStatistic(
                        totalMilliliters = totalMilliliters,
                        favouriteDrinkType = favouriteDrinkType
                    )
                )
            }
        }

    private fun provideDrinkTypeById(waterTrack: WaterTrack): Flow<hardcoder.dev.entities.features.waterTracking.WaterTrack> {
        return drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
            waterTrack.toEntity(drinkType!!)
        }
    }

    private fun WaterTrack.toEntity(drinkType: DrinkType) = WaterTrackEntity(
        id, Instant.fromEpochMilliseconds(date), millilitersCount, drinkType
    )
}