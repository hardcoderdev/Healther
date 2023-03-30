package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.waterTracking.WaterTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(waterTrack: WaterTrack) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.update(
            date = waterTrack.date.toEpochMilliseconds(),
            millilitersCount = waterTrack.millilitersCount,
            drinkTypeId = waterTrack.drinkType.id,
            id = waterTrack.id
        )
    }
}