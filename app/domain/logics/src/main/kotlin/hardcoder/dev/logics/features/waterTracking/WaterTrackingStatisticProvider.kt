package hardcoder.dev.logics.features.waterTracking

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.entities.features.waterTracking.WaterTrackingStatistics
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import kotlin.math.roundToInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import hardcoder.dev.entities.features.waterTracking.WaterTrack as WaterTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideWaterTrackingStatistic() = appDatabase.waterTrackQueries
        .provideAllWaterTracks()
        .asFlow()
        .map {
            it.executeAsList()
        }.flatMapLatest { waterTracksListDatabase ->
            if (waterTracksListDatabase.isEmpty()) {
                flowOf(null)
            } else {
                combine(
                    waterTracksListDatabase.map { waterTrack ->
                        provideDrinkTypeById(waterTrack)
                    },
                ) { waterTrackArray ->
                    val waterTrackList = waterTrackArray.toList()

                    val totalMilliliters = waterTrackList.sumOf { it.millilitersCount }

                    val favouriteDrinkTypeId = waterTrackList.groupingBy { waterTrack ->
                        waterTrack.drinkType
                    }.eachCount().maxBy { entry ->
                        entry.value
                    }.key

                    val averageHydrationIndex =
                        waterTrackList.map { it.drinkType.hydrationIndexPercentage }
                            .average()
                            .roundToInt()

                    val averageWaterIntakes = waterTrackList.groupBy {
                        it.date.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfMonth
                    }.map {
                        it.value.count()
                    }.average().roundToInt()

                    WaterTrackingStatistics(
                        totalMilliliters = totalMilliliters,
                        favouriteDrinkTypeId = favouriteDrinkTypeId,
                        averageHydrationIndex = averageHydrationIndex,
                        averageWaterIntakes = averageWaterIntakes,
                    )
                }
            }
        }.flowOn(dispatchers.io)

    private fun provideDrinkTypeById(waterTrack: WaterTrack): Flow<WaterTrackEntity> {
        return drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
            waterTrack.toEntity(drinkType = drinkType!!)
        }.flowOn(dispatchers.io)
    }

    private fun WaterTrack.toEntity(
        drinkType: DrinkType,
    ) = WaterTrackEntity(
        id = id,
        date = date,
        millilitersCount = millilitersCount,
        drinkType = drinkType,
    )
}