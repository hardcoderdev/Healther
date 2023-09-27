package hardcoder.dev.presentation.features.waterTracking

data class WaterTrackingChartData(
    val entriesList: List<WaterTrackingChartEntry>,
)

data class WaterTrackingChartEntry(
    val from: Int,
    val to: Int,
)
