package hardcoder.dev.logic.features.waterTracking.drinkType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DrinkType
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType as DrinkTypeEntity

class DrinkTypeProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider
) {

    fun provideAllDrinkTypes() = appDatabase.drinkTypeQueries
        .provideAllDrinkTypes()
        .asFlow()
        .map {
            it.executeAsList().map { drinkTypeDatabase ->
                drinkTypeDatabase.toEntity()
            }
        }

    fun provideDrinkTypeById(id: Int) = appDatabase.drinkTypeQueries
        .provideDrinkTypeById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun DrinkType.toEntity() = DrinkTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        hydrationIndexPercentage = hydrationIndexPercentage
    )
}