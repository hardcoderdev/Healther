package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.features.fasting.statistic.FastingStatisticResolver
import hardcoder.dev.androidApp.ui.features.moodTracking.statistic.MoodTrackingStatisticResolver
import hardcoder.dev.androidApp.ui.features.pedometer.statistic.PedometerStatisticResolver
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.statistic.WaterTrackingStatisticResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val resolversModule = module {
    single {
        WaterTrackingStatisticResolver(
            context = androidContext(),
            liquidFormatter = get()
        )
    }

    single {
        PedometerStatisticResolver(
            context = androidContext(),
            dateTimeFormatter = get(),
            decimalFormatter = get()
        )
    }

    single {
        FastingStatisticResolver(
            context = androidContext()
        )
    }

    single {
        MoodTrackingStatisticResolver(
            context = androidContext()
        )
    }
}