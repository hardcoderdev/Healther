package hardcoder.dev.androidApp.ui.features.pedometer.statistic

import android.content.Context
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.uikit.StatisticData
import hardcoderdev.healther.app.android.app.R

class PedometerStatisticResolver(
    private val context: Context,
    private val dateTimeFormatter: DateTimeFormatter,
    private val decimalFormatter: DecimalFormatter
) {

    fun resolve(statistic: PedometerStatistic): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_steps),
                value = statistic.totalSteps.toString()
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_kilometers),
                value = statistic.totalKilometers.let {
                    decimalFormatter.roundAndFormatToString(it)
                }
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_time),
                value = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = statistic.totalDuration.inWholeMilliseconds,
                    accuracy = DateTimeFormatter.Accuracy.MINUTES
                )
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_calories),
                value = decimalFormatter.roundAndFormatToString(statistic.totalCalories)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_steps),
                value = statistic.averageSteps.toString()
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_kilometers),
                value = decimalFormatter.roundAndFormatToString(statistic.averageKilometers)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_time),
                value = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = statistic.averageDuration.inWholeMilliseconds,
                    accuracy = DateTimeFormatter.Accuracy.MINUTES
                )
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_calories),
                value = decimalFormatter.roundAndFormatToString(statistic.averageCalories)
            )
        )
    }
}