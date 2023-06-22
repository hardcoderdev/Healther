package hardcoder.dev.logic.features.waterTracking.drinkType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DrinkType
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType as DrinkTypeEntity

class DrinkTypeProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun provideAllDrinkTypes() = appDatabase.drinkTypeQueries
        .provideAllDrinkTypes()
        .asFlowOfList(dispatchers.io) { drinkTypeDatabase ->
            drinkTypeDatabase.toEntity()
        }

    fun provideDrinkTypeById(id: Int) = appDatabase.drinkTypeQueries
        .provideDrinkTypeById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun DrinkType.toEntity() = DrinkTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        hydrationIndexPercentage = hydrationIndexPercentage
    )
}