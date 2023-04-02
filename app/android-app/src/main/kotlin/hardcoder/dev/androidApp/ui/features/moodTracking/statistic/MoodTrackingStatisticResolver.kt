package hardcoder.dev.androidApp.ui.features.moodTracking.statistic

import android.content.Context
import hardcoder.dev.entities.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.StatisticData

class MoodTrackingStatisticResolver(private val context: Context) {

    fun resolve(
        statistic: MoodTrackingStatistic
    ): List<StatisticData> {
        return listOf(
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_happyMoodCount_text),
                value = statistic.happyMoodCount?.toString()
                    ?: context.getString(R.string.moodTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_neutralMoodCount_text),
                value = statistic.neutralMoodCount?.toString()
                    ?: context.getString(R.string.moodTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_notWellMoodCount_text),
                value = statistic.notWellMoodCount?.toString()
                    ?: context.getString(R.string.moodTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_angryMoodCount_text),
                value = statistic.badMoodCount?.toString()
                    ?: context.getString(R.string.moodTracking_statistic_not_enough_data_text)
            ),
            StatisticData(
                name = context.getString(R.string.moodTracking_statistic_averageMood_text),
                value = statistic.averageMoodType?.name
                    ?: context.getString(R.string.moodTracking_statistic_not_enough_data_text)
            )
        )
    }
}