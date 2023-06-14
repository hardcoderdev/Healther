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
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_total_kilometers),
                value = statistic.totalKilometers?.let {
                    decimalFormatter.roundAndFormatToString(it)
                } ?: context.getString(
                    R.string.pedometer_statistic_not_enough_data_text
                )
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
                value = statistic.totalCalories?.let {
                    decimalFormatter.roundAndFormatToString(it)
                } ?: context.getString(
                    R.string.pedometer_statistic_not_enough_data_text
                )
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_steps),
                value = statistic.averageSteps?.toString()
                    ?: context.getString(R.string.pedometer_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.pedometer_statistic_average_kilometers),
                value = statistic.averageKilometers?.let {
                    decimalFormatter.roundAndFormatToString(it)
                } ?: context.getString(
                    R.string.pedometer_statistic_not_enough_data_text
                )
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
                value = statistic.averageCalories?.let {
                    decimalFormatter.roundAndFormatToString(it)
                } ?: context.getString(
                    R.string.pedometer_statistic_not_enough_data_text
                )
            )
        )
    }
}