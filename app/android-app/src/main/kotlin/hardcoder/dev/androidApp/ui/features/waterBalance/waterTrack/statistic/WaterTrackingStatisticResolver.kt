package hardcoder.dev.androidApp.ui.features.waterBalance.waterTrack.statistic

import android.content.Context
import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.entities.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.StatisticData

class WaterTrackingStatisticResolver(
    private val context: Context,
    private val liquidFormatter: LiquidFormatter
) {

    fun resolve(statistic: WaterTrackingStatistic): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.waterTracking_statistic_water_total_drunk),
                value = statistic.totalMilliliters?.let {
                    liquidFormatter.formatMillisDistance(
                        defaultMilliliters = it,
                        accuracy = LiquidFormatter.Accuracy.MILLILITERS
                    )
                } ?: context.getString(R.string.waterTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_favourite_drink_type),
                value = statistic.favouriteDrinkTypeId?.name
                    ?: context.getString(R.string.waterTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_average_hydration_index),
                value = statistic.averageHydrationIndex?.toString()
                    ?: context.getString(R.string.waterTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_average_water_intakes),
                value = statistic.averageWaterIntakes?.toString()
                    ?: context.getString(R.string.waterTracking_statistic_not_enough_data_text)
            )
        )
    }
}