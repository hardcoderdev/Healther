package hardcoder.dev.androidApp.di.logic.penalty

import hardcoder.dev.logic.reward.penalty.PenaltyCalculator
import hardcoder.dev.logic.reward.penalty.PenaltyCollector
import hardcoder.dev.logic.reward.penalty.PenaltyCreator
import hardcoder.dev.logic.reward.penalty.PenaltyProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val penaltyLogicModule = module {
    singleOf(::PenaltyCalculator)

    single {
        PenaltyCreator(
            idGenerator = get(),
            appDatabase = get(),
            featureTypeIdMapper = get(),
            dispatchers = get(),
        )
    }

    single {
        PenaltyCollector(
            appDatabase = get(),
            dispatchers = get(),
            featureTypeIdMapper = get(),
        )
    }

    single {
        PenaltyProvider(
            appDatabase = get(),
            featureTypeIdMapper = get(),
            dispatchers = get(),
        )
    }
}