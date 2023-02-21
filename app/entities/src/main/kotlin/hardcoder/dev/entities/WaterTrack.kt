package hardcoder.dev.entities

data class WaterTrack(
    val id: Int,
    val date: Long,
    val millilitersCount: Int,
    val drinkType: DrinkType
)