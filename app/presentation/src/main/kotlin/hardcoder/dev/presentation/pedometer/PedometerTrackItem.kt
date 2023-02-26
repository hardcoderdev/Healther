package hardcoder.dev.presentation.pedometer

data class PedometerTrackItem(
    val id: Int,
    val stepsCount: Int,
    val range: LongRange
)
