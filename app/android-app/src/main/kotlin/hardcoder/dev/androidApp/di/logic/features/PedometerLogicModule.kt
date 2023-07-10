package hardcoder.dev.androidApp.di.logic.features

import hardcoder.dev.androidApp.ui.screens.features.pedometer.logic.PedometerManagerImpl
import hardcoder.dev.logic.features.pedometer.DailyRateStepsResolver
import hardcoder.dev.logic.features.pedometer.PedometerStepHandler
import hardcoder.dev.logic.features.pedometer.PedometerStepProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackCreator
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val pedometerLogicModule = module {
    singleOf(::DailyRateStepsResolver)

    single<PedometerManager> {
        PedometerManagerImpl(
            context = androidContext(),
            permissionsController = get(),
            batteryRequirementsController = get(),
        )
    }

    single {
        PedometerTrackCreator(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        PedometerStepHandler(
            idGenerator = get(),
            pedometerTrackCreator = get(),
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