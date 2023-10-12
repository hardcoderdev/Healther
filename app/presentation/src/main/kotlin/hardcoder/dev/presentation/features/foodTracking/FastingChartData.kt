package hardcoder.dev.presentation.features.foodTracking

data class FastingChartData(
    val entriesList: List<FastingChartEntry>,
)

data class FastingChartEntry(
    val from: Int,
    val to: Long,
)
