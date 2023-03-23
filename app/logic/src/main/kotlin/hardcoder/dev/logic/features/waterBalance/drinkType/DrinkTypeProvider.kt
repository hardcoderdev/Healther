package hardcoder.dev.logic.features.waterBalance.drinkType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DrinkType
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.waterTracking.DrinkType as DrinkTypeEntity

class DrinkTypeProvider(private val appDatabase: AppDatabase) {

    fun provideAllDrinkTypes() = appDatabase.drinkTypeQueries
        .selectAllDrinkTypes()
        .asFlow()
        .map {
            it.executeAsList().map { drinkTypeDatabase ->
                drinkTypeDatabase.toEntity()
            }
        }

    fun provideDrinkTypeById(id: Int) = appDatabase.drinkTypeQueries
        .selectDrinkTypeById(id)
        .asFlow()
        .map {
            it.executeAsOne().toEntity()
        }

    private fun DrinkType.toEntity() = DrinkTypeEntity(
        id = id,
        name = name,
        iconResourceName = iconResourceName,
        hydrationIndexPercentage = hydrationIndexPercentage
    )
}