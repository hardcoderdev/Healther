package hardcoder.dev.logic.features.waterTracking.resolvers

import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType

class WaterPercentageResolver {

    fun resolve(drinkType: DrinkType, millilitersDrunk: Int): Int {
        return (millilitersDrunk.toFloat() / 100F * drinkType.hydrationIndexPercentage.toFloat()).toInt()
    }
}