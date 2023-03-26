package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByDrinkTypeId(id: Int) = withContext(dispatcher){
        appDatabase.waterTrackQueries.deleteAllTracksByDrinkTypeId(id)
    }
}