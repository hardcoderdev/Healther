package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.fasting.FastingCreationViewModel
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val fastingPresentationModule = module {
    viewModel {
        FastingViewModel(
            dateTimeProvider = get(),
            fastingTrackProvider = get(),
            statisticProvider = get(),
            currentFastingManager = get(),
        )
    }

    viewModel {
        FastingCreationViewModel(
            currentFastingManager = get(),
            fastingPlanDurationMapper = get(),
            fastingPlanProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        FastingHistoryViewModel(
            fastingTrackProvider = get(),
            dateTimeProvider = get(),
        )
    }
}