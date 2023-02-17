package hardcoder.dev.healther.logic.resolvers

import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType

class DrinkTypeImageResolver {

    fun resolve(drinkType: DrinkType): Int {
        return when (drinkType) {
            DrinkType.WATER -> R.drawable.water
            DrinkType.COFFEE -> R.drawable.coffee
            DrinkType.BEER -> R.drawable.beer
            DrinkType.MILK -> R.drawable.milk
            DrinkType.TEA -> R.drawable.tea
            DrinkType.JUICE -> R.drawable.juice
            DrinkType.SODA -> R.drawable.soda
            DrinkType.SOUP -> R.drawable.soup
        }
    }
}