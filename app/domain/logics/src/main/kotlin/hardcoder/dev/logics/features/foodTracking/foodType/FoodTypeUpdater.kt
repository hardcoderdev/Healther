package hardcoder.dev.logics.features.foodTracking.foodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.foodTracking.CorrectFoodTypeName
import kotlinx.coroutines.withContext

class FoodTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectFoodTypeName,
        icon: Icon,
        isSpicy: Boolean,
        isVegetarian: Boolean,
        proteins: Float,
        fats: Float,
        carbohydrates: Float,
    ) = withContext(dispatchers.io) {
        appDatabase.foodTypeQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
            isSpicy = isSpicy,
            isVegetarian = isVegetarian,
            proteins = proteins,
            fats = fats,
            carbohydrates = carbohydrates,
        )
    }
}