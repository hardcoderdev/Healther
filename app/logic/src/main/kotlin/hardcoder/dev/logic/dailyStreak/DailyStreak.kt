package hardcoder.dev.logic.dailyStreak

import kotlinx.datetime.Instant

data class DailyStreak(
    val id: Int,
    val startDate: Instant,
    val breakdownDate: Instant?,
    val completionDate: Instant?,
)
