package hardcoder.dev.androidApp.di.logic.features

import hardcoder.dev.logic.features.fasting.FastingPenaltyMaker
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.logic.features.fasting.plan.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val fastingLogicModule = module {
    singleOf(::FastingPlanIdMapper)
    singleOf(::FastingPlanProvider)
    singleOf(::FastingPlanDurationResolver)

    single {
        FastingStatisticProvider(
            appDatabase = get(),
            fastingPlanIdMapper = get(),
            dateTimeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        FastingTrackProvider(
            appDatabase = get(),
            fastingPlanIdMapper = get(),
            dispatchers = get(),
            currencyProvider = get(),
        )
    }

    single {
        CurrentFastingManager(
            context = androidContext(),
            appDatabase = get(),
            fastingPlanIdMapper = get(),
            idGenerator = get(),
            fastingTrackProvider = get(),
            diaryTrackCreator = get(),
            dispatchers = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        FastingPenaltyMaker(
            fastingTrackProvider = get(),
            penaltyCreator = get(),
            penaltyCalculator = get(),
            heroHealthPointsManager = get(),
            dateTimeProvider = get(),
            dispatchers = get(),
            lastEntranceManager = get(),
        )
    }
}