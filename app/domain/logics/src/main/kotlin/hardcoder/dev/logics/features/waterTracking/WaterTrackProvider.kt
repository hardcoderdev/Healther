package hardcoder.dev.logics.features.waterTracking

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.WaterTrack
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
    private val appDatabase: AppDatabase,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideWaterTracksByDayRange(dayRange: ClosedRange<Instant>) = appDatabase.waterTrackQueries
        .provideWaterTracksByDayRange(dayRange.start, dayRange.endInclusive)
        .asFlow()
        .map {
            it.executeAsList()
        }.flatMapLatest { waterTracksList ->
            if (waterTracksList.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    waterTracksList.map { waterTrack ->
                        drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
                            waterTrack.toEntity(drinkType = drinkType!!)
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideWaterTrackById(id: Int) = appDatabase.waterTrackQueries
        .provideWaterTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()
        }.flatMapLatest { waterTrack ->
            if (waterTrack == null) {
                flowOf(null)
            } else {
                combine(
                    drinkTypeProvider.provideDrinkTypeById(waterTrack.drinkTypeId).map { drinkType ->
                        waterTrack.toEntity(drinkType = drinkType!!)
                    },
                ) {
                    it[0]
                }
            }
        }.flowOn(dispatchers.io)

    private fun WaterTrack.toEntity(
        drinkType: DrinkType,
    ) = WaterTrackEntity(
        id = id,
        date = date,
        millilitersCount = millilitersCount,
        drinkType = drinkType,
    )
}