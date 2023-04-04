package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.waterTracking.WaterTrackDeleter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeDeleter(
    private val appDatabase: AppDatabase,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        waterTrackDeleter.deleteAllTracksByDrinkTypeId(id)
        appDatabase.drinkTypeQueries.deleteById(id)
    }
}