package hardcoder.dev.logics.features.foodTracking.foodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTypeDao
import hardcoder.dev.database.entities.features.foodTracking.FoodType
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.foodTracking.CorrectFoodTypeName
import kotlinx.coroutines.withContext

class FoodTypeCreator(
    private val foodTypeDao: FoodTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectFoodTypeName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        foodTypeDao.insert(
            FoodType(
                name = name.data,
                iconId = icon.id,
            ),
        )
    }
}