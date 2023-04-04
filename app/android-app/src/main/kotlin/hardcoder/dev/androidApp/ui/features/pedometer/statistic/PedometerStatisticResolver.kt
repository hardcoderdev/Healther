package hardcoder.dev.androidApp.ui.features.pedometer.statistic

import android.content.Context
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.extensions.roundAndFormatToString
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.uikit.StatisticData

class PedometerStatisticResolver(
    private val context: Context,
    private val dateTimeFormatter: DateTimeFormatter
) {

    fun resolve(statistic: PedometerStatistic): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_steps),
                value = statistic.totalSteps?.toString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_kilometers),
                value = statistic.totalKilometers?.roundAndFormatToString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_time),
                value = statistic.totalDuration?.let {
                    dateTimeFormatter.formatMillisDistance(
                        distanceInMillis = it.inWholeMilliseconds,
                        accuracy = DateTimeFormatter.Accuracy.MINUTES
                    )
                } ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_calories),
                value = statistic.totalCalories?.roundAndFormatToString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_steps),
                value = statistic.averageSteps?.toString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_kilometers),
                value = statistic.averageKilometers?.roundAndFormatToString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_time),
                value = statistic.averageDuration?.let {
                    dateTimeFormatter.formatMillisDistance(
                        distanceInMillis = it.inWholeMilliseconds,
                        accuracy = DateTimeFormatter.Accuracy.MINUTES
                    )
                } ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_calories),
                value = statistic.averageCalories?.roundAndFormatToString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            )
        )
    }
}