package hardcoder.dev.logic.waterBalance

import hardcoder.dev.entities.DrinkType

class DrinkTypeIdMapper {

    fun mapToId(drinkType: DrinkType) = when(drinkType) {
        DrinkType.WATER -> 0
        DrinkType.COFFEE -> 1
        DrinkType.BEER -> 2
        DrinkType.MILK -> 3
        DrinkType.TEA -> 4
        DrinkType.JUICE -> 5
        DrinkType.SODA -> 6
        DrinkType.SOUP -> 7
    }

    fun mapToDrinkType(drinkId: Int) = when (drinkId) {
        0 -> DrinkType.WATER
        1 -> DrinkType.COFFEE
        2 -> DrinkType.BEER
        3 -> DrinkType.MILK
        4 -> DrinkType.TEA
        5 -> DrinkType.JUICE
        6 -> DrinkType.SODA
        7 -> DrinkType.SOUP
        else -> DrinkType.WATER
    }
}