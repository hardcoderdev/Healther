package hardcoder.dev.presentation.pedometer

data class PedometerTrackItem(
    val id: Int,
    val stepsCount: Int,
    val caloriesCount: Float,
    val wastedTimeInMinutes: Int,
    val date: Long
)
