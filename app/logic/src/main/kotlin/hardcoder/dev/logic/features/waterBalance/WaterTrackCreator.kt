package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.waterTracking.DrinkType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val drinkTypeIdMapper: DrinkTypeIdMapper
) {

    suspend fun createWaterTrack(
        date: Long,
        millilitersCount: Int,
        drinkType: DrinkType
    ) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.insert(
            id = null,
            date = date,
            millilitersCount = millilitersCount,
            drinkTypeId = drinkTypeIdMapper.mapToId(drinkType)
        )
    }
}