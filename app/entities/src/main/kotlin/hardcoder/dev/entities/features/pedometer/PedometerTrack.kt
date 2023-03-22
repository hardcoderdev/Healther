package hardcoder.dev.entities.pedometer

data class PedometerTrack(
    val id: Int,
    val stepsCount: Int,
    val range: LongRange
)
