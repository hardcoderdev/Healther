package hardcoder.dev.mock.dataProviders

import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem

object DashboardMockDataProvider {

    fun dashboardWaterTrackingFeature() = DashboardFeatureItem.WaterTrackingFeature(
        progress = 0.3f,
        millilitersDrunk = WaterTrackingMockDataProvider.millilitersDrunkToDailyRate(),
    )

    fun dashboardPedometerFeature() = DashboardFeatureItem.PedometerFeature(
        progress = 0.5f,
        stepsWalked = 5000,
        dailyRateInSteps = 10_000,
        isPedometerRunning = true,
        isPermissionsGranted = true,
    )

    fun dashboardMoodTrackingFeature() = DashboardFeatureItem.MoodTrackingFeature(
        tracksCount = 19,
        tracksDailyRate = 20,
        progress = 0.9f,
    )

    fun dashboardDiaryFeature() = DashboardFeatureItem.DiaryFeature(
        tracksCount = 0,
        tracksDailyRate = 3,
        progress = 0.0f,
    )

    fun dashboardFeatureSectionsList() = listOf(
        dashboardWaterTrackingFeature(),
        dashboardPedometerFeature(),
        dashboardMoodTrackingFeature(),
        dashboardDiaryFeature(),
    )
}