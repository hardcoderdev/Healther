package hardcoder.dev.logics.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.DrinkTypeDao
import hardcoder.dev.database.entities.features.waterTracking.DrinkType
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.waterTracking.CorrectDrinkTypeName
import kotlinx.coroutines.withContext

class DrinkTypeCreator(
    private val drinkTypeDao: DrinkTypeDao,
    private val predefinedDrinkTypeProvider: PredefinedDrinkTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectDrinkTypeName,
        icon: Icon,
        hydrationIndexInPercentage: Int,
    ) = withContext(dispatchers.io) {
        drinkTypeDao.insert(
            DrinkType(
                name = name.data,
                iconId = icon.id,
                hydrationIndexInPercentage = hydrationIndexInPercentage,
            ),
        )
    }

    // TODO PRE-POPULATE CALLBACK
    suspend fun createPredefined() = withContext(dispatchers.io) {
        predefinedDrinkTypeProvider.providePredefined().forEach { drinkType ->
            drinkTypeDao.insert(
                DrinkType(
                    name = drinkType.name,
                    iconId = drinkType.icon.id,
                    hydrationIndexInPercentage = drinkType.hydrationIndexPercentage,
                )
            )
        }
    }
}