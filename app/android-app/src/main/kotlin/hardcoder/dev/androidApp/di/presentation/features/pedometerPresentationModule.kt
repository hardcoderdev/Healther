package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pedometerPresentationModule = module {
    viewModel {
        PedometerViewModel(
            pedometerManager = get(),
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
        )
    }

    viewModel {
        PedometerHistoryViewModel(
            pedometerTrackProvider = get(),
            pedometerStatisticProvider = get(),
        )
    }
}