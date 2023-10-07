package hardcoder.dev.entities.features.waterTracking

import kotlinx.datetime.Instant

data class WaterTrack(
    val id: Int,
    val date: Instant,
    val millilitersCount: Int,
    val drinkType: DrinkType,
)