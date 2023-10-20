package hardcoder.dev.logics.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.DrinkTypeDao
import hardcoder.dev.database.entities.features.waterTracking.DrinkType
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.waterTracking.CorrectDrinkTypeName
import kotlinx.coroutines.withContext

class DrinkTypeUpdater(
    private val drinkTypeDao: DrinkTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectDrinkTypeName,
        icon: Icon,
        hydrationIndexInPercentage: Int,
    ) = withContext(dispatchers.io) {
        drinkTypeDao.update(
            DrinkType(
                id = id,
                name = name.data,
                iconId = icon.id,
                hydrationIndexInPercentage = hydrationIndexInPercentage,
            )
        )
    }
}