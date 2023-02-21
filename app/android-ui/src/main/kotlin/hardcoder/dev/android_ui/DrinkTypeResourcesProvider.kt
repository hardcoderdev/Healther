package hardcoder.dev.android_ui

import hardcoder.dev.entities.DrinkType
import hardcoder.dev.healther.R

class DrinkTypeResourcesProvider {

    fun provide(drinkType: DrinkType) = DrinkTypeResources(
        title = when (drinkType) {
            DrinkType.WATER -> R.string.waterTrackItem_waterDrinkType_text
            DrinkType.COFFEE -> R.string.waterTrackItem_coffeeDrinkType_text
            DrinkType.BEER -> R.string.waterTrackItem_beerDrinkType_text
            DrinkType.MILK -> R.string.waterTrackItem_milkDrinkType_text
            DrinkType.TEA -> R.string.waterTrackItem_teaDrinkType_text
            DrinkType.JUICE -> R.string.waterTrackItem_juiceDrinkType_text
            DrinkType.SODA -> R.string.waterTrackItem_sodaDrinkType_text
            DrinkType.SOUP -> R.string.waterTrackItem_soupDrinkType_text
        },
        image = when (drinkType) {
            DrinkType.WATER -> R.drawable.water
            DrinkType.COFFEE -> R.drawable.coffee
            DrinkType.BEER -> R.drawable.beer
            DrinkType.MILK -> R.drawable.milk
            DrinkType.TEA -> R.drawable.tea
            DrinkType.JUICE -> R.drawable.juice
            DrinkType.SODA -> R.drawable.soda
            DrinkType.SOUP -> R.drawable.soup
        },
        drinkType = drinkType
    )
}