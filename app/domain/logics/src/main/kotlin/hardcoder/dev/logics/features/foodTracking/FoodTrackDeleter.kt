package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class FoodTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) {
        appDatabase.foodTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByFoodTypeId(foodTypeId: Int) = withContext(dispatchers.io) {
        appDatabase.foodTrackQueries.deleteAllTracksByFoodTypeId(foodTypeId)
    }
}