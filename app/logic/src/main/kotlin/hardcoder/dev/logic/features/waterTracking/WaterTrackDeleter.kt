package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class WaterTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        appDatabase.waterTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByDrinkTypeId(id: Int) = withContext(dispatchers.io) {
        appDatabase.waterTrackQueries.deleteAllTracksByDrinkTypeId(id)
    }
}