package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.waterTracking.WaterTrackDeleter
import kotlinx.coroutines.withContext

class DrinkTypeDeleter(
    private val appDatabase: AppDatabase,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        waterTrackDeleter.deleteAllTracksByDrinkTypeId(id)
        appDatabase.drinkTypeQueries.deleteById(id)
    }
}