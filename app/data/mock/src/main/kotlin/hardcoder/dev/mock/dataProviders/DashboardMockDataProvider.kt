package hardcoder.dev.mock.dataProviders

import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.presentation.dashboard.DashboardHeroItem

object DashboardMockDataProvider {

    fun dashboardHeroSection() = DashboardHeroItem.HeroSection(
        experiencePointsProgress = 0.3f,
        experiencePointsToNextLevel = 40f,
        healthPointsProgress = 0.5f,
        hero = HeroMockDataProvider.hero(),
    )

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

    fun dashboardFastingFeature() = DashboardFeatureItem.FastingFeature(
        timeLeftDuration = MockDateProvider.duration(),
        planDuration = MockDateProvider.duration(),
        progress = 0.7f,
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
        dashboardFastingFeature(),
        dashboardMoodTrackingFeature(),
        dashboardDiaryFeature(),
    )
}