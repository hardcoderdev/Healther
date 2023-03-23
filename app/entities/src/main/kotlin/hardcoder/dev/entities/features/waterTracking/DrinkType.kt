package hardcoder.dev.entities.features.waterTracking

data class DrinkType(
    val id: Int,
    val name: String,
    val iconResourceName: String,
    val hydrationIndexPercentage: Int
)