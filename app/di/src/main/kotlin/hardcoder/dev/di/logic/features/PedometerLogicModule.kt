package hardcoder.dev.di.logic.features

import hardcoder.dev.logic.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logic.features.pedometer.PedometerStepHandler
import hardcoder.dev.logic.features.pedometer.PedometerStepProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackUpserter
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val pedometerLogicModule = module {
    singleOf(::PedometerDailyRateStepsProvider)

//    single<PedometerManager> {
//        PedometerManagerImpl(
//            context = androidContext(),
//            permissionsController = get(),
//            batteryRequirementsController = get(),
//        )
//    }

    single {
        PedometerTrackUpserter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        PedometerStepHandler(
            idGenerator = get(),
            pedometerTrackUpserter = get(),
        )
    }

    single {
        PedometerStepProvider(
            pedometerTrackProvider = get(),
            dispatchers = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        PedometerTrackProvider(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        PedometerStatisticProvider(
            kilometersResolver = get(),
            caloriesResolver = get(),
            pedometerTrackProvider = get(),
            dispatchers = get(),
        )
    }
}