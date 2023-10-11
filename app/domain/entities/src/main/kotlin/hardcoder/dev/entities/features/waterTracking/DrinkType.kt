package hardcoder.dev.entities.features.waterTracking

import hardcoder.dev.icons.Icon

data class DrinkType(
    val id: Int,
    val name: String,
    val icon: Icon,
    val hydrationIndexPercentage: Int,
)