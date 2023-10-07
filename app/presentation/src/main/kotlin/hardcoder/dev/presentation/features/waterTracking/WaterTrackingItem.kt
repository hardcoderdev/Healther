package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType

data class WaterTrackingItem(
    val id: Int,
    val drinkType: DrinkType,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long,
)