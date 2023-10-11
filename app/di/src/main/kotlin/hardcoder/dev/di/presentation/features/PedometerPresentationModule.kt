package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import org.koin.dsl.module

internal val pedometerPresentationModule = module {
    factory {
        PedometerViewModel(
            pedometerManager = get(),
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
            dateTimeProvider = get(),
            pedometerDailyRateStepsProvider = get(),
        )
    }

    factory {
        PedometerHistoryViewModel(
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
            dateTimeProvider = get(),
        )
    }
}