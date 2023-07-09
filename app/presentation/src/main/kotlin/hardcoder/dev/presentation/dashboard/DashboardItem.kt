package hardcoder.dev.presentation.dashboard

import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.waterTracking.MillilitersDrunkToDailyRate
import kotlin.time.Duration

sealed class DashboardItem {
    data class DiaryFeature(
        val diaryTrackList: List<DiaryTrack>,
        val dailyRate: Int,
        val progress: Float,
    ) : DashboardItem()

    data class WaterTrackingFeature(
        val millilitersDrunk: MillilitersDrunkToDailyRate,
        val progress: Float,
    ) : DashboardItem()

    data class PedometerFeature(
        val permissionsGranted: Boolean,
        val isPedometerRunning: Boolean,
        val stepsWalked: Int,
        val dailyRateInSteps: Int,
        val progress: Float,
    ) : DashboardItem()

    data class FastingFeature(
        val timeLeftDuration: Duration?,
        val planDuration: Duration?,
        val progress: Float,
    ) : DashboardItem()

    data class MoodTrackingFeature(
        val averageMoodToday: MoodType?,
    ) : DashboardItem()
}