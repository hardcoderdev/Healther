package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.icons.Icon
import kotlinx.coroutines.withContext

class DrinkTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectDrinkTypeName,
        icon: Icon,
        hydrationIndexPercentage: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.drinkTypeQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
            hydrationIndexPercentage = hydrationIndexPercentage,
        )
    }
}