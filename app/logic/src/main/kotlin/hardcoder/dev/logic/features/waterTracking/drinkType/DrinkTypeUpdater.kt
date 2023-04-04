package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(drinkType: DrinkType) = withContext(dispatcher) {
        appDatabase.drinkTypeQueries.update(
            id = drinkType.id,
            name = drinkType.name,
            iconId = drinkType.icon.id,
            hydrationIndexPercentage = drinkType.hydrationIndexPercentage
        )
    }
}