package hardcoder.dev.logics.features.foodTracking.foodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.foodTracking.FoodTypeDao
import hardcoder.dev.database.entities.features.foodTracking.FoodType
import hardcoder.dev.icons.IconResourceProvider
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.foodTracking.FoodType as FoodTypeEntity

class FoodTypeProvider(
    private val foodTypeDao: FoodTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val iconResourceProvider: IconResourceProvider,
) {

    fun provideAllFoodTypes() = foodTypeDao
        .provideAllFoodTypes()
        .mapItems { it.toEntity() }
        .flowOn(dispatchers.io)

    fun provideFoodTypeById(id: Int) = foodTypeDao
        .provideFoodTypeById(id)
        .map { it?.toEntity() }
        .flowOn(dispatchers.io)

    private fun FoodType.toEntity() = FoodTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}