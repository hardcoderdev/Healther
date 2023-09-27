package hardcoder.dev.logic.features.moodTracking.statistic

data class MoodTrackingChartData(
    val entriesList: List<MoodTrackingChartEntry>,
)

data class MoodTrackingChartEntry(
    val from: Int,
    val to: Int,
)
