package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.logic.entities.features.waterTracking.DrinkType

data class WaterTrackItem(
    val id: Int,
    val drinkType: DrinkType,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long
)