package hardcoder.dev.healther.logic.providers

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.healther.R
import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.database.WaterTrack
import hardcoder.dev.healther.entities.DrinkType
import hardcoder.dev.healther.ui.base.composables.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class WaterTrackProvider(private val appDatabase: AppDatabase) {

    fun provideWaterTracksByDayRange(dayRange: LongRange) = appDatabase.waterTrackQueries
        .selectWaterTracksByDayRange(dayRange.first, dayRange.last)
        .asFlow()
        .map {
            it.executeAsList()
        }

    fun provideWaterTrackById(id: Int) = appDatabase.waterTrackQueries
        .selectWaterTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    fun getAllDrinkTypes(): Flow<List<Drink>> {
        return flow {
            emit(
                listOf(
                    Drink(type = DrinkType.WATER, image = R.drawable.water),
                    Drink(type = DrinkType.TEA, image = R.drawable.tea),
                    Drink(type = DrinkType.COFFEE, image = R.drawable.coffee),
                    Drink(type = DrinkType.BEER, image = R.drawable.beer),
                    Drink(type = DrinkType.JUICE, image = R.drawable.juice),
                    Drink(type = DrinkType.SODA, image = R.drawable.soda),
                    Drink(type = DrinkType.SOUP, image = R.drawable.soup),
                    Drink(type = DrinkType.MILK, image = R.drawable.milk)
                )
            )
        }
    }

    private fun WaterTrack.toEntity() = hardcoder.dev.healther.entities.WaterTrack(
        id, date, millilitersCount, drinkType
    )
}