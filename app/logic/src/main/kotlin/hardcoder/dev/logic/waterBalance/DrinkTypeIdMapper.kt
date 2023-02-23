package hardcoder.dev.logic.waterBalance

import hardcoder.dev.entities.waterTracking.DrinkType

class DrinkTypeIdMapper {

    private val drinkTypeIds = mapOf(
        DrinkType.WATER to 0,
        DrinkType.COFFEE to 1,
        DrinkType.BEER to 2,
        DrinkType.MILK to 3,
        DrinkType.TEA to 4,
        DrinkType.JUICE to 5,
        DrinkType.SODA to 6,
        DrinkType.SOUP to 7
    )

    fun mapToId(drinkType: DrinkType) = checkNotNull(drinkTypeIds[drinkType])

    fun mapToDrinkType(drinkId: Int) = checkNotNull(
        drinkTypeIds.entries.find { it.value == drinkId }
    ).key
}