package hardcoder.dev.logics.features.foodTracking.foodType

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FoodType
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.foodTracking.FoodType as FoodTypeEntity

class FoodTypeProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val iconResourceProvider: IconResourceProvider,
) {

    fun provideAllFoodTypes() = appDatabase.foodTypeQueries
        .provideAllFoodTypes()
        .asFlowOfList(dispatchers.io) { foodTypeDatabase ->
            foodTypeDatabase.toEntity()
        }

    fun provideFoodTypeById(id: Int) = appDatabase.foodTypeQueries
        .provideFoodTypeById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun FoodType.toEntity() = FoodTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        isSpicy = isSpicy,
        isVegetarian = isVegetarian,
        proteins = proteins,
        fats = fats,
        carbohydrates = carbohydrates,
    )
}