package hardcoder.dev.logic.features.moodTracking.statistic

import hardcoder.dev.logic.features.moodTracking.moodType.MoodType

data class MoodTrackingStatistic(
    val happyMoodCount: Int,
    val neutralMoodCount: Int,
    val notWellMoodCount: Int,
    val badMoodCount: Int,
    val averageMoodType: MoodType,
)