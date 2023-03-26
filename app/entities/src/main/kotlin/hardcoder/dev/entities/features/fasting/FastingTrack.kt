package hardcoder.dev.entities.features.fasting

data class FastingTrack(
    val id: Int,
    val startTime: Long,
    val duration: Long,
    val fastingPlan: FastingPlan,
    val interruptedTime: Long?
)