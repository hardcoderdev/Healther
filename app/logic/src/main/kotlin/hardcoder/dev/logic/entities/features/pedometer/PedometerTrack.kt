package hardcoder.dev.logic.entities.features.pedometer

import kotlinx.datetime.Instant

data class PedometerTrack(
    val id: Int,
    val stepsCount: Int,
    val range: ClosedRange<Instant>
)
