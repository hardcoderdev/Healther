package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(waterTrack: WaterTrack) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.update(
            date = waterTrack.date,
            millilitersCount = waterTrack.millilitersCount,
            drinkTypeId = waterTrack.drinkType.id,
            id = waterTrack.id
        )
    }
}