package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTrackDao
import hardcoder.dev.database.entities.features.foodTracking.FoodTrack
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class FoodTrackCreator(
    private val foodTrackDao: FoodTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        creationInstant: Instant,
        calories: Int,
        foodTypeId: Int,
    ) = withContext(dispatchers.io) {
        foodTrackDao.insert(
            FoodTrack(
                calories = calories,
                creationInstant = creationInstant,
                foodTypeId = foodTypeId,
            ),
        )
    }
}