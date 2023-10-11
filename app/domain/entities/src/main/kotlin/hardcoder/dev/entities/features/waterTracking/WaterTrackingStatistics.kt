package hardcoder.dev.entities.features.waterTracking

data class WaterTrackingStatistics(
    val totalMilliliters: Int,
    val favouriteDrinkTypeId: DrinkType,
    val averageHydrationIndex: Int,
    val averageWaterIntakes: Int,
)