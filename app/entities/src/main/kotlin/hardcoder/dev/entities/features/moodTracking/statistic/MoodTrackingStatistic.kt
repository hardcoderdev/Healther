package hardcoder.dev.entities.features.moodTracking.statistic

import hardcoder.dev.entities.features.moodTracking.MoodType

data class MoodTrackingStatistic(
    val happyMoodCount: Int?,
    val neutralMoodCount: Int?,
    val notWellMoodCount: Int?,
    val badMoodCount: Int?,
    val averageMoodType: MoodType?
)
