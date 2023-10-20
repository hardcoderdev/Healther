package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
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
    private val waterTrackDao: WaterTrackDao,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideWaterTrackingStatistic() = waterTrackDao
        .provideAllWaterTracks()
        .flatMapLatest { waterTracksListDatabase ->
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
                        it.creationInstant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfMonth
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
            waterTrack.toEntity(drinkType = drinkType)
        }.flowOn(dispatchers.io)
    }

    private fun WaterTrack.toEntity(
        drinkType: DrinkType,
    ) = WaterTrackEntity(
        id = id,
        creationInstant = creationInstant,
        millilitersCount = millilitersCount,
        drinkType = drinkType,
    )
}