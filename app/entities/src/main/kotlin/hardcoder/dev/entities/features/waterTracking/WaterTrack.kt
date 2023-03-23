package hardcoder.dev.entities.features.waterTracking

data class WaterTrack(
    val id: Int,
    val date: Long,
    val millilitersCount: Int,
    val drinkType: DrinkType
)