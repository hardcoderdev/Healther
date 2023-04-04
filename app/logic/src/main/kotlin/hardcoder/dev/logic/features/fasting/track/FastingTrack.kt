package hardcoder.dev.logic.features.fasting.track

import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import kotlinx.datetime.Instant
import kotlin.time.Duration

data class FastingTrack(
    val id: Int,
    val startTime: Instant,
    val duration: Duration,
    val fastingPlan: FastingPlan,
    val interruptedTime: Instant?
)