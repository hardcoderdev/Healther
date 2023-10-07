package hardcoder.dev.di.logic.features

import hardcoder.dev.logic.features.fasting.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.FastingTrackProvider
import hardcoder.dev.mappers.features.fasting.FastingPlanIdMapper
import hardcoder.dev.resolvers.features.fasting.FastingPlanDurationResolver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val fastingLogicModule = module {
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
}