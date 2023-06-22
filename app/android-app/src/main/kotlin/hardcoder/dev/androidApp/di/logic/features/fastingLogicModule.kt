package hardcoder.dev.androidApp.di.logic.features

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
            fastingTrackProvider = get(),
            dateTimeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        FastingTrackProvider(
            appDatabase = get(),
            fastingPlanIdMapper = get(),
            dispatchers = get(),
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
        )
    }
}