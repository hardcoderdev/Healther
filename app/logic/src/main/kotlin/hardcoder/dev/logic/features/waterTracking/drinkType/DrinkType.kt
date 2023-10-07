package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.icons.Icon

data class DrinkType(
    val id: Int,
    val name: String,
    val icon: Icon,
    val hydrationIndexPercentage: Int,
)