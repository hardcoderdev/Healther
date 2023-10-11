package hardcoder.dev.di.logic.features

import hardcoder.dev.logics.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logics.features.pedometer.PedometerStepHandler
import hardcoder.dev.logics.features.pedometer.PedometerStepProvider
import hardcoder.dev.logics.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logics.features.pedometer.PedometerTrackUpserter
import hardcoder.dev.logics.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.pedometer_manager.PedometerManagerImpl
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val pedometerLogicModule = module {
    singleOf(::PedometerDailyRateStepsProvider)

    single<PedometerManager> {
        PedometerManagerImpl(
            context = androidContext(),
            permissionsController = get(),
            batteryRequirementsController = get(),
        )
    }

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