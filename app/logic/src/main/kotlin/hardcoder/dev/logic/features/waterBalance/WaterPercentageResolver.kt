package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.entities.features.waterTracking.DrinkType

class WaterPercentageResolver {

    fun resolve(drinkType: DrinkType, millilitersDrunk: Int): Int {
        return (millilitersDrunk.toFloat() / 100F * drinkType.hydrationIndexPercentage.toFloat()).toInt()
    }
}