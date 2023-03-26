package hardcoder.dev.logic.features.waterBalance.drinkType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.waterTracking.DrinkType
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
            iconResourceName = drinkType.iconResourceName,
            hydrationIndexPercentage = drinkType.hydrationIndexPercentage
        )
    }
}