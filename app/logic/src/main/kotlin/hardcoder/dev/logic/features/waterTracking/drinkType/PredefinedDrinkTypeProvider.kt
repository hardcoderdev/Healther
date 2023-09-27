package hardcoder.dev.logic.features.waterTracking.drinkType

interface PredefinedDrinkTypeProvider {
    fun providePredefined(): List<DrinkTypePredefined>
}

data class DrinkTypePredefined(
    val name: String,
    val icon: hardcoder.dev.icons.Icon,
    val hydrationIndexPercentage: Int,
)