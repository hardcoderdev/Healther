package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.identification.IdGenerator
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class FoodTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        creationInstant: Instant,
        calories: Int,
        foodTypeId: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.foodTrackQueries.insert(
            id = idGenerator.nextId(),
            calories = calories,
            creationInstant = creationInstant,
            foodTypeId = foodTypeId,
        )
    }
}