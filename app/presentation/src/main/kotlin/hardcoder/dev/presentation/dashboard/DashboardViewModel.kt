package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.pedometer.DailyRateStepsResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.toggleTracking
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DashboardViewModel(
    private val dailyRateStepsResolver: DailyRateStepsResolver,
    private val pedometerManager: PedometerManager,
    private val dateTimeProvider: DateTimeProvider,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val currentFastingManager: CurrentFastingManager,
    private val moodTrackProvider: MoodTrackProvider,
) : ViewModel() {

    val itemsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            diaryItem(),
            waterTrackingItem(),
            pedometerItem(),
            fastingItem(),
            moodTrackingItemFlow(),
        ) { diaryItem, waterTrackingItem, pedometerItem, fastingItem, moodTrackingItem ->
            listOf(
                diaryItem,
                waterTrackingItem,
                pedometerItem,
                fastingItem,
                moodTrackingItem,
            )
        },
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = viewModelScope,
        toggle = pedometerManager::toggleTracking,
        isActiveFlow = pedometerManager.isTracking,
    )

    private fun diaryItem() = diaryTrackProvider
        .provideAllDiaryTracksByDateRange(
            dateRange = dateTimeProvider.currentDateRange(),
        ).map { diaryTrackList ->
            val isTrackCreatedToday = diaryTrackList.isNotEmpty()
            DashboardItem.DiaryFeature(
                diaryTrackList = diaryTrackList,
                dailyRate = if (isTrackCreatedToday) 1 else 0,
                progress = if (isTrackCreatedToday) 1.0f else 0.0f,
            )
        }

    private fun waterTrackingItem() = waterTrackingMillilitersDrunkProvider
        .provideMillilitersDrunkToDailyRateToday()
        .map { millilitersDrunkToDailyRate ->
            DashboardItem.WaterTrackingFeature(
                millilitersDrunk = millilitersDrunkToDailyRate,
                progress = millilitersDrunkToDailyRate.millilitersDrunkCount safeDiv millilitersDrunkToDailyRate.dailyWaterIntake,
            )
        }

    private fun pedometerItem() = combine(
        pedometerTrackProvider.providePedometerTracksByRange(dateTimeProvider.currentDateRange()),
        pedometerManager.isTracking,
        pedometerManager.availability,
    ) { pedometerTrackList, isPedometerRunning, availability ->
        val stepsWalked = pedometerTrackList.sumOf { it.stepsCount }
        val dailyRateInSteps = dailyRateStepsResolver.resolve()
        DashboardItem.PedometerFeature(
            stepsWalked = stepsWalked,
            dailyRateInSteps = dailyRateInSteps,
            isPedometerRunning = isPedometerRunning,
            permissionsGranted = availability is Available,
            progress = stepsWalked safeDiv dailyRateInSteps,
        )
    }

    private fun fastingItem() = combine(
        dateTimeProvider.currentTimeFlow(),
        currentFastingManager.provideCurrentFastingTrack(),
    ) { currentTime, currentFastingTrack ->
        currentFastingTrack?.let {
            val timeLeftInMillis = currentTime.toInstant(
                TimeZone.currentSystemDefault(),
            ) - currentFastingTrack.startTime

            DashboardItem.FastingFeature(
                timeLeftDuration = timeLeftInMillis,
                planDuration = currentFastingTrack.duration,
                progress = timeLeftInMillis.inWholeMilliseconds safeDiv requireNotNull(
                    currentFastingTrack.duration,
                ).inWholeMilliseconds,
            )
        } ?: DashboardItem.FastingFeature(
            timeLeftDuration = null,
            planDuration = null,
            progress = 0.0f,
        )
    }

    private fun moodTrackingItemFlow() = moodTrackProvider.provideAllMoodTracksByDayRange(
        dateTimeProvider.currentDateRange(),
    ).map { moodTrackList ->
        DashboardItem.MoodTrackingFeature(
            averageMoodToday = moodTrackList.groupingBy {
                it.moodType
            }.eachCount().maxByOrNull {
                it.value
            }?.key,
        )
    }
}