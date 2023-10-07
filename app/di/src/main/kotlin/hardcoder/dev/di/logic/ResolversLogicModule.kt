package hardcoder.dev.di.logic

import hardcoder.dev.resolvers.features.pedometer.CaloriesResolver
import hardcoder.dev.resolvers.features.pedometer.KilometersResolver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val resolversLogicModule = module {
    singleOf(::KilometersResolver)
    singleOf(::CaloriesResolver)
}