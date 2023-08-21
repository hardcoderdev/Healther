package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.reward.penalty.PenaltyCalculator
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val resolversLogicModule = module {
    singleOf(::KilometersResolver)
    singleOf(::CaloriesResolver)

    single {
        PenaltyCalculator()
    }
}