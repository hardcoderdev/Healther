package hardcoder.dev.logics.features.foodTracking.foodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTypeDao
import kotlinx.coroutines.withContext

class FoodTypeDeleter(
    private val foodTypeDao: FoodTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        foodTypeDao.deleteById(id)
    }
}