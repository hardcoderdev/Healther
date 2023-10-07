package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticResolver
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.analytics.MoodTrackingStatisticResolver
import hardcoder.dev.androidApp.ui.screens.features.pedometer.statistic.PedometerStatisticResolver
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics.WaterTrackingStatisticResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val resolversModule = module {
    single {
        WaterTrackingStatisticResolver(
            context = androidContext(),
            liquidFormatter = get(),
        )
    }

    single {
        PedometerStatisticResolver(
            context = androidContext(),
            millisDistanceFormatter = get(),
            decimalFormatter = get(),
        )
    }

    single {
        FastingStatisticResolver(
            context = androidContext(),
        )
    }

    single {
        MoodTrackingStatisticResolver(
            context = androidContext(),
        )
    }
}