package hardcoder.dev.logic.reward.experience

import kotlinx.datetime.Instant

data class Experience(
    val id: Int,
    val date: Instant,
    val amount: Float,
    val isCollected: Boolean,
)