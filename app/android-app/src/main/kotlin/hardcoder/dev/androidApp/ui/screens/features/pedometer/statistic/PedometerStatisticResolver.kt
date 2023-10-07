package hardcoder.dev.androidApp.ui.screens.features.pedometer.statistic

import android.content.Context
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.uikit.components.statistic.StatisticData
import hardcoderdev.healther.app.resources.R

class PedometerStatisticResolver(
    private val context: Context,
    private val millisDistanceFormatter: hardcoder.dev.formatters.MillisDistanceFormatter,
    private val decimalFormatter: hardcoder.dev.formatters.DecimalFormatter,
) {

    fun resolve(statistic: PedometerStatistic): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_steps),
                value = statistic.totalSteps.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_kilometers),
                value = statistic.totalKilometers.let {
                    decimalFormatter.roundAndFormatToString(it)
                },
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_time),
                value = millisDistanceFormatter.formatMillisDistance(
                    distanceInMillis = statistic.totalDuration.inWholeMilliseconds,
                    accuracy = hardcoder.dev.formatters.MillisDistanceFormatter.Accuracy.MINUTES,
                ),
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_calories),
                value = decimalFormatter.roundAndFormatToString(statistic.totalCalories),
            ),
        )
    }
}