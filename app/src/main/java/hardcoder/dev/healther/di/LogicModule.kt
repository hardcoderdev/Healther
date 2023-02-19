package hardcoder.dev.healther.di

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

    val waterTrackMillilitersValidator by lazy {
        WaterTrackMillilitersValidator()
    }
}