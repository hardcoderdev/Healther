package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class FoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        creationInstant: Instant,
        foodTypeId: Int,
        calories: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.foodTrackQueries.update(
            id = id,
            creationInstant = creationInstant,
            foodTypeId = foodTypeId,
            calories = calories,
        )
    }
}