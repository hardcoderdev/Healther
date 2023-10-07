package hardcoder.dev.resolvers.features.pedometer

import android.content.Context
import hardcoder.dev.entities.features.pedometer.PedometerStatistics
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.uikit.components.statistics.StatisticData
import hardcoderdev.healther.app.ui.resources.R

class PedometerStatisticResolver(
    private val context: Context,
    private val millisDistanceFormatter: MillisDistanceFormatter,
    private val decimalFormatter: DecimalFormatter,
) {

    fun resolve(statistic: PedometerStatistics): List<StatisticData> {
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
                    accuracy = MillisDistanceFormatter.Accuracy.MINUTES,
                ),
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_calories),
                value = decimalFormatter.roundAndFormatToString(statistic.totalCalories),
            ),
        )
    }
}