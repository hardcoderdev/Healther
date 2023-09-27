package hardcoder.dev.presentation.features.pedometer

data class PedometerChartData(
   val entriesList: List<PedometerChartEntry>
)

data class PedometerChartEntry(
    val from: Int,
    val to: Int,
)
