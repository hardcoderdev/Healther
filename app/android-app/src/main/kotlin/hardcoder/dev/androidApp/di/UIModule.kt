package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.features.fasting.statistic.FastingStatisticResolver
import hardcoder.dev.androidApp.ui.features.moodTracking.statistic.MoodTrackingStatisticResolver
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerRejectedMapper
import hardcoder.dev.androidApp.ui.features.pedometer.statistic.PedometerStatisticResolver
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.statistic.WaterTrackingStatisticResolver
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider

class UIModule(private val context: Context) {

    val dateTimeFormatter by lazy {
        DateTimeFormatter(
            context = context,
            defaultAccuracy = DateTimeFormatter.Accuracy.MINUTES
        )
    }

    val genderResourcesProvider by lazy {
        GenderResourcesProvider()
    }

    val liquidFormatter by lazy {
        LiquidFormatter(
            context = context,
            defaultAccuracy = LiquidFormatter.Accuracy.MILLILITERS
        )
    }

    val waterTrackingStatisticResolver by lazy {
        WaterTrackingStatisticResolver(
            context = context,
            liquidFormatter = liquidFormatter
        )
    }

    val pedometerStatisticResolver by lazy {
        PedometerStatisticResolver(
            context = context,
            dateTimeFormatter = dateTimeFormatter
        )
    }

    val pedometerRejectedMapper by lazy {
        PedometerRejectedMapper()
    }

    val fastingPlanResourcesProvider by lazy {
        FastingPlanResourcesProvider()
    }

    val fastingStatisticResolver by lazy {
        FastingStatisticResolver(
            context = context
        )
    }

    val moodTrackingStatisticResolver by lazy {
        MoodTrackingStatisticResolver(
            context = context
        )
    }
}