package hardcoder.dev.entities.features.fasting

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class FastingTrack(
    val id: Int,
    val startTime: Instant,
    val duration: Duration,
    val fastingPlan: FastingPlan,
    val interruptedTime: Instant?
)