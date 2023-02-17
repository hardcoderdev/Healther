package hardcoder.dev.healther.di

import hardcoder.dev.healther.logic.resolvers.DrinkTypeImageResolver
import hardcoder.dev.healther.logic.resolvers.WaterIntakeResolver
import hardcoder.dev.healther.logic.resolvers.WaterPercentageResolver
import hardcoder.dev.healther.logic.validators.WaterTrackMillilitersValidator

class LogicModule {

    val waterPercentageResolver by lazy {
        WaterPercentageResolver()
    }

    val waterIntakeResolver by lazy {
        WaterIntakeResolver()
    }

    val drinkTypeImageResolver by lazy {
        DrinkTypeImageResolver()
    }

    val waterTrackMillilitersValidator by lazy {
        WaterTrackMillilitersValidator()
    }
}