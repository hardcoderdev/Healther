package hardcoder.dev.logics.features.waterTracking.drinkType

import hardcoder.dev.entities.features.waterTracking.DrinkTypePredefined

interface PredefinedDrinkTypeProvider {
    fun providePredefined(): List<DrinkTypePredefined>
}