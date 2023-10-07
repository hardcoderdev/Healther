package hardcoder.dev.logic.features.fasting.track

import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import kotlin.time.Duration
import kotlinx.datetime.Instant

data class FastingTrack(
    val id: Int,
    val startTime: Instant,
    val duration: Duration,
    val fastingPlan: FastingPlan,
    val interruptedTime: Instant?,
    val fastingProgress: Float,
)