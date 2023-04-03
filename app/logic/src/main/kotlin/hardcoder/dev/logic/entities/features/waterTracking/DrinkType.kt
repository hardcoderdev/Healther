package hardcoder.dev.logic.entities.features.waterTracking

import hardcoder.dev.logic.icons.LocalIcon

data class DrinkType(
    val id: Int,
    val name: String,
    val icon: LocalIcon,
    val hydrationIndexPercentage: Int
)