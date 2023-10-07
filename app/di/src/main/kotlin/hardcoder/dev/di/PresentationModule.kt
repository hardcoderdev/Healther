package hardcoder.dev.di

import hardcoder.dev.di.presentation.dashboardPresentationModule
import hardcoder.dev.di.presentation.features.diaryPresentationModule
import hardcoder.dev.di.presentation.features.fastingPresentationModule
import hardcoder.dev.di.presentation.features.moodTrackingPresentationModule
import hardcoder.dev.di.presentation.features.pedometerPresentationModule
import hardcoder.dev.di.presentation.features.waterTrackingPresentationModule
import hardcoder.dev.di.presentation.heroPresentationModule
import hardcoder.dev.di.presentation.settingsPresentationModule
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