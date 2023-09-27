package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.withContext

class DrinkTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val predefinedDrinkTypeProvider: PredefinedDrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectDrinkTypeName,
        icon: hardcoder.dev.icons.Icon,
        hydrationIndexPercentage: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.drinkTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
            hydrationIndexPercentage = hydrationIndexPercentage,
        )
    }

    suspend fun createPredefined() = withContext(dispatchers.io) {
        predefinedDrinkTypeProvider.providePredefined().forEach { drinkType ->
            appDatabase.drinkTypeQueries.insert(
                id = idGenerator.nextId(),
                name = drinkType.name,
                iconId = drinkType.icon.id,
                hydrationIndexPercentage = drinkType.hydrationIndexPercentage,
            )
        }
    }
}