package hardcoder.dev.logics.features.foodTracking.foodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.icons.Icon
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.validators.features.foodTracking.CorrectFoodTypeName
import kotlinx.coroutines.withContext

class FoodTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectFoodTypeName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.foodTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
        )
    }
}