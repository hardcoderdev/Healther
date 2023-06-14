package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackDeleter(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        appDatabase.waterTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByDrinkTypeId(id: Int) = withContext(ioDispatcher){
        appDatabase.waterTrackQueries.deleteAllTracksByDrinkTypeId(id)
    }
}