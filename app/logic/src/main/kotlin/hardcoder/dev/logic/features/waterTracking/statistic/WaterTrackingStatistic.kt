package hardcoder.dev.logic.features.waterTracking.statistic

import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType

data class WaterTrackingStatistic(
    val totalMilliliters: Int,
    val favouriteDrinkTypeId: DrinkType,
    val averageHydrationIndex: Int,
    val averageWaterIntakes: Int,
)