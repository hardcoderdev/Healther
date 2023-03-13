package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.waterTracking.WaterTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val drinkTypeIdMapper: DrinkTypeIdMapper
) {

    suspend fun update(waterTrack: WaterTrack) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.update(
            date = waterTrack.date,
            millilitersCount = waterTrack.millilitersCount,
            drinkTypeId = drinkTypeIdMapper.mapToId(waterTrack.drinkType),
            id = waterTrack.id
        )
    }
}