package hardcoder.dev.presentation.waterBalance

import hardcoder.dev.entities.waterTracking.DrinkType

data class WaterTrackItem(
    val id: Int,
    val drinkType: DrinkType,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long
)