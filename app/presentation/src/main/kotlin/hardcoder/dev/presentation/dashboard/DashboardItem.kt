package hardcoder.dev.presentation.dashboard

import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import kotlin.time.Duration

sealed class DashboardItem {
    data class WaterTrackingFeature(
        val millilitersDrunk: Int,
        val dailyRateInMilliliters: Int
    ) : DashboardItem()

    data class PedometerFeature(
        val permissionsGranted: Boolean,
        val isPedometerRunning: Boolean,
        val stepsWalked: Int,
        val dailyRateInSteps: Int
    ) : DashboardItem()

    data class FastingFeature(
        val timeLeftDuration: Duration?,
        val planDuration: Duration?
    ) : DashboardItem()

    data class MoodTrackingFeature(
        val averageMoodToday: MoodType?
    ) : DashboardItem()
}