package hardcoder.dev.entities.pedometer

data class PedometerTrack(
    val id: Int,
    val stepsCount: Int,
    val caloriesCount: Float,
    val wastedTimeInMinutes: Int,
    val date: Long
)
