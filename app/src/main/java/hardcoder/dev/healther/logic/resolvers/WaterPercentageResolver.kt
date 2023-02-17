package hardcoder.dev.healther.logic.resolvers

import hardcoder.dev.healther.data.local.room.entities.DrinkType

class WaterPercentageResolver {

    fun resolve(drinkType: DrinkType, millilitersDrunk: Int): Int {
        return when (drinkType) {
            DrinkType.WATER -> millilitersDrunk
            DrinkType.COFFEE -> millilitersDrunk - (millilitersDrunk / 100 * 50)
            DrinkType.BEER -> millilitersDrunk / 100 * 90
            DrinkType.MILK -> millilitersDrunk / 100 * 80
            DrinkType.TEA -> millilitersDrunk - (millilitersDrunk / 100 * 30)
            DrinkType.JUICE -> millilitersDrunk / 100 * 80
            DrinkType.SODA -> millilitersDrunk / 100 * 90
            DrinkType.SOUP -> millilitersDrunk / 100 * 50
        }
    }
}