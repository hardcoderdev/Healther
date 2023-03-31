package hardcoder.dev.entities.features.waterTracking.statistic

import hardcoder.dev.entities.features.waterTracking.DrinkType

data class WaterTrackingStatistic(
    val totalMilliliters: Int?,
    val favouriteDrinkTypeId: DrinkType?,
    val averageHydrationIndex: Int?,
    val averageWaterIntakes: Int?
)
