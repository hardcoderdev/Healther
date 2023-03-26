package hardcoder.dev.entities.features.pedometer

data class PedometerTrack(
    val id: Int,
    val stepsCount: Int,
    val range: LongRange
)
