package hardcoder.dev.androidApp.di

import hardcoder.dev.androidApp.di.presentation.dashboardPresentationModule
import hardcoder.dev.androidApp.di.presentation.features.diaryPresentationModule
import hardcoder.dev.androidApp.di.presentation.features.fastingPresentationModule
import hardcoder.dev.androidApp.di.presentation.features.moodTrackingPresentationModule
import hardcoder.dev.androidApp.di.presentation.features.pedometerPresentationModule
import hardcoder.dev.androidApp.di.presentation.features.waterTrackingPresentationModule
import hardcoder.dev.androidApp.di.presentation.heroPresentationModule
import hardcoder.dev.androidApp.di.presentation.settingsPresentationModule
import org.koin.dsl.module

val presentationModule = module {
    includes(
        heroPresentationModule,
        dashboardPresentationModule,
        settingsPresentationModule,
        waterTrackingPresentationModule,
        pedometerPresentationModule,
        fastingPresentationModule,
        moodTrackingPresentationModule,
        diaryPresentationModule,
    )
}