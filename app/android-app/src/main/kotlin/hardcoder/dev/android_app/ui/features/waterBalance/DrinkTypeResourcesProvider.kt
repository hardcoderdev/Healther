package hardcoder.dev.android_app.ui.features.waterBalance

import hardcoder.dev.entities.waterTracking.DrinkType
import hardcoder.dev.healther.R

class DrinkTypeResourcesProvider {

    private val map = mapOf(
        DrinkType.WATER to listOf(
            R.string.waterTracking_drinkType_water,
            R.drawable.drink_type_water
        ),
        DrinkType.COFFEE to listOf(
            R.string.waterTracking_drinkType_coffee,
            R.drawable.drink_type_coffee
        ),
        DrinkType.BEER to listOf(
            R.string.waterTracking_drinkType_beer,
            R.drawable.drink_type_beer
        ),
        DrinkType.MILK to listOf(
            R.string.waterTracking_drinkType_milk,
            R.drawable.drink_type_milk
        ),
        DrinkType.TEA to listOf(
            R.string.waterTracking_drinkType_tea,
            R.drawable.drink_type_tea
        ),
        DrinkType.JUICE to listOf(
            R.string.waterTracking_drinkType_juice,
            R.drawable.drink_type_juice
        ),
        DrinkType.SODA to listOf(
            R.string.waterTracking_drinkType_soda,
            R.drawable.drink_type_soda
        ),
        DrinkType.SOUP to listOf(
            R.string.waterTracking_drinkType_soup,
            R.drawable.drink_type_soup
        ),
    )

    fun provide(drinkType: DrinkType) = checkNotNull(map[drinkType]).let {
        DrinkTypeResources(
            title = it[0],
            image = it[1],
            drinkType = drinkType
        )
    }
}