package hardcoder.dev.logic.features.waterTracking.drinkType

import hardcoder.dev.logic.icons.LocalIcon

data class DrinkType(
    val id: Int,
    val name: String,
    val icon: LocalIcon,
    val hydrationIndexPercentage: Int
)