package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTrackDao
import kotlinx.coroutines.withContext

class FoodTrackDeleter(
    private val foodTrackDao: FoodTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) {
        foodTrackDao.deleteById(id)
    }

    suspend fun deleteAllTracksByFoodTypeId(foodTypeId: Int) = withContext(dispatchers.io) {
        foodTrackDao.deleteAllTracksByFoodTypeId(foodTypeId)
    }
}