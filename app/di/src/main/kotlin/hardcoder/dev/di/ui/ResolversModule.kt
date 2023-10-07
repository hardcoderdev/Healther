package hardcoder.dev.di.ui

import hardcoder.dev.resolvers.features.fasting.FastingStatisticResolver
import hardcoder.dev.resolvers.features.moodTracking.MoodTrackingStatisticResolver
import hardcoder.dev.resolvers.features.pedometer.PedometerStatisticResolver
import hardcoder.dev.resolvers.features.waterTracking.WaterTrackingStatisticResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val resolversModule = module {
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