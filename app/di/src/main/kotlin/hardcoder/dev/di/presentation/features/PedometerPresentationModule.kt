package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val pedometerPresentationModule = module {
    single {
        PedometerViewModel(
            pedometerManager = get(),
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
            dateTimeProvider = get(),
            pedometerDailyRateStepsProvider = get(),
        )
    }

    single {
        PedometerHistoryViewModel(
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
            dateTimeProvider = get(),
        )
    }
}