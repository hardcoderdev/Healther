package hardcoder.dev.entities.features.waterTracking.statistic

import hardcoder.dev.entities.features.waterTracking.DrinkType

data class WaterTrackingStatistic(
    val totalMilliliters: Int?,
    val favouriteDrinkType: DrinkType?
)
