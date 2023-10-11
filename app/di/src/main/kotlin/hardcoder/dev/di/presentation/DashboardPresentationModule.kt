package hardcoder.dev.di.presentation

import hardcoder.dev.presentation.dashboard.DashboardViewModel
import org.koin.dsl.module

internal val dashboardPresentationModule = module {
    factory {
        DashboardViewModel(
            pedometerDailyRateStepsProvider = get(),
            pedometerManager = get(),
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