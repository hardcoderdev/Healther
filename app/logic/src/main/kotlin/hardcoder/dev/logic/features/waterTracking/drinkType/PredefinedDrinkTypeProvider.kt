package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.logic.icons.LocalIcon

interface PredefinedDrinkTypeProvider {
    fun providePredefined(): List<DrinkTypePredefined>
}

data class DrinkTypePredefined(
    val name: String,
    val icon: LocalIcon,
    val hydrationIndexPercentage: Int
)