package hardcoder.dev.androidApp.ui.screens.features.fasting.statistic

import android.content.Context

import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.uikit.components.statistic.StatisticData
import hardcoderdev.healther.app.android.app.R

class FastingStatisticResolver(private val context: Context) {

    fun resolve(
        statistic: FastingStatistic,
        favouritePlanResId: Int?,
    ): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.fasting_statistic_max_hours_text),
                value = statistic.duration?.maximumDurationInHours?.toString()
                    ?: context.getString(R.string.fasting_statistic_not_enough_data_text),
            ),
            StatisticData(
                name = context.getString(R.string.fasting_statistic_min_hours_text),
                value = statistic.duration?.minimumDurationInHours?.toString()
                    ?: context.getString(R.string.fasting_statistic_not_enough_data_text),
            ),
            StatisticData(
                name = context.getString(R.string.fasting_statistic_average_hours_text),
                value = statistic.duration?.averageDurationInHours?.toString()
                    ?: context.getString(R.string.fasting_statistic_not_enough_data_text),
            ),
            StatisticData(
                name = context.getString(R.string.fasting_statistic_favorite_plan_text),
                value = favouritePlanResId?.let { context.getString(favouritePlanResId) }
                    ?: context.getString(R.string.fasting_statistic_not_enough_data_text),
            ),
        )
    }
}