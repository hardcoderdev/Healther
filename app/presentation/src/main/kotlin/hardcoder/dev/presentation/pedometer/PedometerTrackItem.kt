package hardcoder.dev.presentation.pedometer

data class PedometerTrackItem(
    val stepsCount: Int,
    val kilometersCount: Float,
    val caloriesBurnt: Float,
    val timeInMillis: Long
)
