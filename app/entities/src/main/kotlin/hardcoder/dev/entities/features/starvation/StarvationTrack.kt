package hardcoder.dev.entities.features.starvation

data class StarvationTrack(
    val id: Int,
    val startTime: Long,
    val duration: Long,
    val starvationPlan: StarvationPlan,
    val interruptedTime: Long?
)