package hardcoder.dev.presentation.dashboard

import hardcoder.dev.entities.features.waterTracking.MillilitersDrunkToDailyRate

sealed class DashboardFeatureItem {
    data class WaterTrackingFeature(
        val millilitersDrunk: MillilitersDrunkToDailyRate,
        val progress: Float,
    ) : DashboardFeatureItem()

    data class PedometerFeature(
        val isPermissionsGranted: Boolean,
        val isPedometerRunning: Boolean,
        val stepsWalked: Int,
        val dailyRateInSteps: Int,
        val progress: Float,
    ) : DashboardFeatureItem()

    data class MoodTrackingFeature(
        val tracksCount: Int,
        val tracksDailyRate: Int,
        val progress: Float,
    ) : DashboardFeatureItem()

    data object FoodTrackingFeature : DashboardFeatureItem()

    data class DiaryFeature(
        val tracksCount: Int,
        val tracksDailyRate: Int,
        val progress: Float,
    ) : DashboardFeatureItem()
}