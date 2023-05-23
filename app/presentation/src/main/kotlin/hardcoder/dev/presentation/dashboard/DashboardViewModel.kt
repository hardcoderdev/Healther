package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DashboardViewModel(
    private val dailyRateStepsResolver: DailyRateStepsResolver,
    private val pedometerManager: PedometerManager,
    dateTimeProvider: DateTimeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
    waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    pedometerTrackProvider: PedometerTrackProvider,
    currentFastingManager: CurrentFastingManager,
    moodTrackProvider: MoodTrackProvider
) : ViewModel() {

    private val waterTrackingFeature = combine(
        waterTrackingDailyRateProvider.provideDailyRateInMilliliters(),
        waterTrackingMillilitersDrunkProvider.provideMillilitersDrunkToday()
    ) { dailyRateInMilliliters, millilitersDrunk ->
        DashboardItem.WaterTrackingFeature(
            millilitersDrunk = millilitersDrunk,
            dailyRateInMilliliters = dailyRateInMilliliters
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val pedometerFeature = combine(
        pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay()
        ),
        pedometerManager.isTracking,
        pedometerManager.availability
    ) { pedometerTrackList, isPedometerRunning, availability ->
        val dailyRateInSteps = dailyRateStepsResolver.resolve()
        val stepsWalked = pedometerTrackList.sumOf { it.stepsCount }

        DashboardItem.PedometerFeature(
            stepsWalked = stepsWalked,
            dailyRateInSteps = dailyRateInSteps,
            isPedometerRunning = isPedometerRunning,
            permissionsGranted = availability is Available
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val fastingFeature = combine(
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
        } ?: run {
            DashboardItem.FastingFeature(
                timeLeftDuration = null,
                planDuration = null,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val moodTrackingFeature = moodTrackProvider.provideAllMoodTracksByDayRange(
        LocalDate.now().createRangeForCurrentDay()
    ).map { moodTrackList ->
        if (moodTrackList.isNotEmpty()) {
            val averageMoodToday = moodTrackList.groupingBy {
                it.moodType
            }.eachCount().maxBy {
                it.value
            }.key

            DashboardItem.MoodTrackingFeature(averageMoodToday = averageMoodToday)
        } else {
            DashboardItem.MoodTrackingFeature(averageMoodToday = null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        waterTrackingFeature.filterNotNull(),
        pedometerFeature.filterNotNull(),
        fastingFeature.filterNotNull(),
        moodTrackingFeature.filterNotNull()
    ) { waterTrackingFeature, pedometerFeature, fastingFeature, moodTrackingFeature ->
        State(
            dashboardItems = listOf(
                waterTrackingFeature,
                pedometerFeature,
                fastingFeature,
                moodTrackingFeature
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            dashboardItems = emptyList()
        )
    )

    fun onTogglePedometerTrackingService() {
        viewModelScope.launch {
            pedometerManager.toggleTracking()
        }
    }

    data class State(
        val dashboardItems: List<DashboardItem>
    )
}