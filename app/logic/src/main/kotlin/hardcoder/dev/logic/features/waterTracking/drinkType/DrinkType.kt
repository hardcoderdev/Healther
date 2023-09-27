package hardcoder.dev.logic.features.waterTracking.drinkType

data class DrinkType(
    val id: Int,
    val name: String,
    val icon: hardcoder.dev.icons.Icon,
    val hydrationIndexPercentage: Int,
)