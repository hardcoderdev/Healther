package hardcoder.dev.androidApp.ui.screens.features.moodTracking.analytics

import android.content.Context

import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.uikit.components.statistic.StatisticData
import hardcoderdev.healther.app.resources.R

class MoodTrackingStatisticResolver(private val context: Context) {

    fun resolve(
        statistic: MoodTrackingStatistic,
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