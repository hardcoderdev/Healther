package hardcoder.dev.entities.features.moodTracking

data class MoodTrackingChartData(
    val entriesList: List<MoodTrackingChartEntry>,
)

data class MoodTrackingChartEntry(
    val from: Int,
    val to: Int,
)
