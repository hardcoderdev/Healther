package hardcoder.dev.di.presentation

import hardcoder.dev.presentation.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val dashboardPresentationModule = module {
    viewModel {
        DashboardViewModel(
            pedometerDailyRateStepsProvider = get(),
            //pedometerManager = get(), TODO UNCOMMENT
            dateTimeProvider = get(),
            waterTrackingMillilitersDrunkProvider = get(),
            pedometerTrackProvider = get(),
            moodTrackProvider = get(),
            moodTrackDailyRateProvider = get(),
            diaryTrackProvider = get(),
            diaryDailyRateProvider = get(),
        )
    }
}