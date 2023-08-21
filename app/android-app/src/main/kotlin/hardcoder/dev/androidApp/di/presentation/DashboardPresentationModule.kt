package hardcoder.dev.androidApp.di.presentation

import hardcoder.dev.presentation.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModel {
        DashboardViewModel(
            pedometerDailyRateStepsProvider = get(),
            pedometerManager = get(),
            dateTimeProvider = get(),
            waterTrackingMillilitersDrunkProvider = get(),
            pedometerTrackProvider = get(),
            currentFastingManager = get(),
            moodTrackProvider = get(),
            moodTrackDailyRateProvider = get(),
            diaryTrackProvider = get(),
            diaryDailyRateProvider = get(),
            heroHealthPointsResolver = get(),
            heroProvider = get(),
            heroExperiencePointsProvider = get(),
            heroExperiencePointsResolver = get(),
        )
    }
}