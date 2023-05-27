package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.pedometer.DailyRateStepsResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.toggleTracking
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DashboardViewModel(
    private val dailyRateStepsResolver: DailyRateStepsResolver,
    private val pedometerManager: PedometerManager,
    private val dateTimeProvider: DateTimeProvider,
    private val waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
    private val waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val currentFastingManager: CurrentFastingManager,
    private val moodTrackProvider: MoodTrackProvider
) : ViewModel() {

    val itemsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            waterTrackingItem(),
            pedometerItem(),
            fastingItem(),
            moodTrackingItemFlow()
        ) { waterTrackingItem, pedometerItem, fastingItem, moodTrackingItem ->
            listOf(
                waterTrackingItem,
                pedometerItem,
                fastingItem,
                moodTrackingItem
            )
        }
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = viewModelScope,
        toggle = pedometerManager::toggleTracking,
        isActiveFlow = pedometerManager.isTracking
    )

    private fun waterTrackingItem() = waterTrackingMillilitersDrunkProvider
        .provideMillilitersDrunkToDailyRateToday()
        .map(DashboardItem::WaterTrackingFeature)

    private fun pedometerItem() = combine(
        pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay()
        ),
        pedometerManager.isTracking,
        pedometerManager.availability
    ) { pedometerTrackList, isPedometerRunning, availability ->
        DashboardItem.PedometerFeature(
            stepsWalked = pedometerTrackList.sumOf { it.stepsCount },
            dailyRateInSteps = dailyRateStepsResolver.resolve(),
            isPedometerRunning = isPedometerRunning,
            permissionsGranted = availability is Available
        )
    }

    private fun fastingItem() = combine(
        dateTimeProvider.currentTimeFlow(),
        currentFastingManager.provideCurrentFastingTrack()
    ) { currentTime, currentFastingTrack ->
        currentFastingTrack?.let {
            val timeLeftInMillis = currentTime.toInstant(
                TimeZone.currentSystemDefault()
            ) - currentFastingTrack.startTime

            DashboardItem.FastingFeature(
                timeLeftDuration = timeLeftInMillis,
                planDuration = currentFastingTrack.duration,
            )
        } ?: DashboardItem.FastingFeature(
            timeLeftDuration = null,
            planDuration = null,
        )
    }

    private fun moodTrackingItemFlow() = moodTrackProvider.provideAllMoodTracksByDayRange(
        LocalDate.now().createRangeForCurrentDay()
    ).map { moodTrackList ->
        DashboardItem.MoodTrackingFeature(
            averageMoodToday = moodTrackList.groupingBy {
                it.moodType
            }.eachCount().maxByOrNull {
                it.value
            }?.key
        )
    }
}