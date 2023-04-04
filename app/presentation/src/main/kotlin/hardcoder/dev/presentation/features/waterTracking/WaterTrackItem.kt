package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType

data class WaterTrackItem(
    val id: Int,
    val drinkType: DrinkType,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long
)