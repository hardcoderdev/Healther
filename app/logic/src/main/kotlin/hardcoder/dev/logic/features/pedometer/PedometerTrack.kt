package hardcoder.dev.logic.features.pedometer

import kotlinx.datetime.Instant

data class PedometerTrack(
    val id: Int,
    val stepsCount: Int,
    val range: ClosedRange<Instant>,
)