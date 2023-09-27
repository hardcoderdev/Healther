package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics

import android.content.Context

import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.uikit.components.statistic.StatisticData
import hardcoderdev.healther.app.resources.R

class WaterTrackingStatisticResolver(
    private val context: Context,
    private val liquidFormatter: LiquidFormatter,
) {

    fun resolve(statistic: WaterTrackingStatistic): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.waterTracking_statistic_water_total_drunk),
                value = liquidFormatter.formatMillisDistance(
                    defaultMilliliters = statistic.totalMilliliters,
                    accuracy = LiquidFormatter.Accuracy.MILLILITERS,
                ),
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_favourite_drink_type),
                value = statistic.favouriteDrinkTypeId.name,
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_average_hydration_index),
                value = statistic.averageHydrationIndex.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.waterTracking_average_water_intakes),
                value = statistic.averageWaterIntakes.toString(),
            ),
        )
    }
}