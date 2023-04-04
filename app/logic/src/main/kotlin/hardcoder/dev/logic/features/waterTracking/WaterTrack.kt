package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import kotlinx.datetime.Instant

data class WaterTrack(
    val id: Int,
    val date: Instant,
    val millilitersCount: Int,
    val drinkType: DrinkType
)