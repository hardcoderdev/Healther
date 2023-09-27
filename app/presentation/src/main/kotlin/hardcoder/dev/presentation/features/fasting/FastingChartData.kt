package hardcoder.dev.presentation.features.fasting

data class FastingChartData(
    val entriesList: List<FastingChartEntry>,
)

data class FastingChartEntry(
    val from: Int,
    val to: Long,
)
