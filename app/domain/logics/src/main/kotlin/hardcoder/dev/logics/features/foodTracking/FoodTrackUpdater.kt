package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTrackDao
import hardcoder.dev.database.entities.features.foodTracking.FoodTrack
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class FoodTrackUpdater(
    private val foodTrackDao: FoodTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        creationInstant: Instant,
        foodTypeId: Int,
        calories: Int,
    ) = withContext(dispatchers.io) {
        foodTrackDao.update(
            FoodTrack(
                id = id,
                creationInstant = creationInstant,
                foodTypeId = foodTypeId,
                calories = calories,
            ),
        )
    }
}