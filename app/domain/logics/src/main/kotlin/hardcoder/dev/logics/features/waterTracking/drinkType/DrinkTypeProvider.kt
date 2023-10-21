package hardcoder.dev.logics.features.waterTracking.drinkType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.waterTracking.DrinkTypeDao
import hardcoder.dev.database.entities.features.waterTracking.DrinkType
import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.IconResourceProvider
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.waterTracking.DrinkType as DrinkTypeEntity

class DrinkTypeProvider(
    private val drinkTypeDao: DrinkTypeDao,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDrinkTypes() = drinkTypeDao
        .provideAllDrinkTypes()
        .mapItems { it.toEntity(icon = iconResourceProvider.getIcon(it.iconId)) }
        .flowOn(dispatchers.io)

    fun provideDrinkTypeById(id: Int) = drinkTypeDao
        .provideDrinkTypeById(id)
        .map { it?.toEntity(icon = iconResourceProvider.getIcon(it.iconId)) }
        .flowOn(dispatchers.io)
}

private fun DrinkType.toEntity(icon: Icon) = DrinkTypeEntity(
    id = id,
    name = name,
    icon = icon,
    hydrationIndexPercentage = hydrationIndexInPercentage,
)