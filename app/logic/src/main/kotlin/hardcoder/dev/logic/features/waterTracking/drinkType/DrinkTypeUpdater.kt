package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(
         id: Int,
         name: CorrectDrinkTypeName,
         icon: LocalIcon,
         hydrationIndexPercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.drinkTypeQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
            hydrationIndexPercentage = hydrationIndexPercentage
        )
    }
}