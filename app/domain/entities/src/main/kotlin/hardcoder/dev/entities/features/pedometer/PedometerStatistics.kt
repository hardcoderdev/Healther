package hardcoder.dev.entities.features.pedometer

import kotlin.time.Duration

data class PedometerStatistics(
    val totalSteps: Int,
    val totalKilometers: Float,
    val totalDuration: Duration,
    val totalCalories: Float,
)