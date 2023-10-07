package hardcoder.dev.entities.features.fasting

import kotlin.time.Duration
import kotlinx.datetime.Instant

data class FastingTrack(
    val id: Int,
    val startTime: Instant,
    val duration: Duration,
    val fastingPlan: hardcoder.dev.entities.features.fasting.FastingPlan,
    val interruptedTime: Instant?,
    val fastingProgress: Float,
)