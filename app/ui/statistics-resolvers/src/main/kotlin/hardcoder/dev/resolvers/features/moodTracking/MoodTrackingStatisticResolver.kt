package hardcoder.dev.resolvers.features.moodTracking

import android.content.Context

import hardcoder.dev.entities.features.moodTracking.MoodTrackingStatistics
import hardcoder.dev.uikit.components.statistics.StatisticData
import hardcoderdev.healther.app.ui.resources.R

class MoodTrackingStatisticResolver(private val context: Context) {

    fun resolve(
        statistic: MoodTrackingStatistics,
    ): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_happyMoodCount_text),
                value = statistic.happyMoodCount.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_neutralMoodCount_text),
                value = statistic.neutralMoodCount.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_notWellMoodCount_text),
                value = statistic.notWellMoodCount.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_angryMoodCount_text),
                value = statistic.badMoodCount.toString(),
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_averageMood_text),
                value = statistic.averageMoodType.name,
            ),
        )
    }
}