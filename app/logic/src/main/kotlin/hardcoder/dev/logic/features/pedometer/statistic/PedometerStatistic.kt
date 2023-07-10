package hardcoder.dev.logic.features.pedometer.statistic

import kotlin.time.Duration

data class PedometerStatistic(
    val totalSteps: Int,
    val totalKilometers: Float,
    val totalDuration: Duration,
    val totalCalories: Float,
)