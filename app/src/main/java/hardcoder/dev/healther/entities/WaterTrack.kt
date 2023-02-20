package hardcoder.dev.healther.entities

data class WaterTrack(
    val id: Int,
    val date: Long,
    val millilitersCount: Int,
    val drinkType: DrinkType
)