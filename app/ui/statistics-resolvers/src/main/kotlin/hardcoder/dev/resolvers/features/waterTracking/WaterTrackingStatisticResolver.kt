package hardcoder.dev.resolvers.features.waterTracking

import android.content.Context
import hardcoder.dev.blocks.components.statistics.StatisticData
import hardcoder.dev.entities.features.waterTracking.WaterTrackingStatistics
import hardcoder.dev.formatters.LiquidFormatter
import hardcoderdev.healther.app.ui.resources.R

class WaterTrackingStatisticResolver(
    private val context: Context,
    private val liquidFormatter: LiquidFormatter,
) {

    fun resolve(statistic: WaterTrackingStatistics): List<StatisticData> {
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