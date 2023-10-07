package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.icons.Icon

interface PredefinedDrinkTypeProvider {
    fun providePredefined(): List<DrinkTypePredefined>
}

data class DrinkTypePredefined(
    val name: String,
    val icon: Icon,
    val hydrationIndexPercentage: Int,
)