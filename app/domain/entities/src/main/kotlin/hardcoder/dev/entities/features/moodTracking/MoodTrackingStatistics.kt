package hardcoder.dev.entities.features.moodTracking

data class MoodTrackingStatistics(
    val happyMoodCount: Int,
    val neutralMoodCount: Int,
    val notWellMoodCount: Int,
    val badMoodCount: Int,
    val averageMoodType: MoodType,
)