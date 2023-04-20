package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val predefinedDrinkTypeProvider: PredefinedDrinkTypeProvider
) {

    suspend fun create(
        name: String,
        icon: LocalIcon,
        hydrationIndexPercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.drinkTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name,
            iconId = icon.id,
            hydrationIndexPercentage = hydrationIndexPercentage
        )
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        predefinedDrinkTypeProvider.providePredefined().forEach { drinkType ->
            appDatabase.drinkTypeQueries.insert(
                id = idGenerator.nextId(),
                name = drinkType.name,
                iconId = drinkType.icon.id,
                hydrationIndexPercentage = drinkType.hydrationIndexPercentage
            )
        }
    }
}