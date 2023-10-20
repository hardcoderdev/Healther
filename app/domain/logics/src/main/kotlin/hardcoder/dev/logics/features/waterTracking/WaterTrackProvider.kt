package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.waterTracking.WaterTrack as WaterTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackProvider(
    private val waterTrackDao: WaterTrackDao,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideWaterTracksByDayRange(dayRange: ClosedRange<Instant>) = waterTrackDao
        .provideWaterTracksByDayRange(
            startDate = dayRange.start,
            endDate = dayRange.endInclusive,
        )
        .flatMapLatest { waterTracksList ->
            if (waterTracksList.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    waterTracksList.map { waterTrack ->
                        drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
                            waterTrack.toEntity(drinkType = drinkType)
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideWaterTrackById(id: Int) = waterTrackDao
        .provideWaterTrackById(id)
        .flatMapLatest { waterTrack ->
            if (waterTrack == null) {
                flowOf(null)
            } else {
                drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
                    waterTrack.toEntity(drinkType)
                }
            }
        }.flowOn(dispatchers.io)

    private fun WaterTrack.toEntity(
        drinkType: DrinkType,
    ) = WaterTrackEntity(
        id = id,
        creationInstant = creationInstant,
        millilitersCount = millilitersCount,
        drinkType = drinkType,
    )
}