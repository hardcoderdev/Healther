package hardcoder.dev.healther.di

import hardcoder.dev.healther.logic.WaterIntakeResolver
import hardcoder.dev.healther.logic.WaterPercentageResolver

class LogicModule {

    val waterPercentageResolver by lazy {
        WaterPercentageResolver()
    }

    val waterIntakeResolver by lazy {
        WaterIntakeResolver()
    }
}