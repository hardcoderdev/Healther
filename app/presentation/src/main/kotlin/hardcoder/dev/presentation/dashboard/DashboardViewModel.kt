package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.dashboard.DashboardItem
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.pedometer.DailyRateStepsResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterIntakeResolver
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

class DashboardViewModel(
    private val waterIntakeResolver: WaterIntakeResolver,
    private val dailyRateStepsResolver: DailyRateStepsResolver,
    private val pedometerManager: PedometerManager,
    private val waterPercentageResolver: WaterPercentageResolver,
    private val fastingPlanDurationResolver: FastingPlanDurationResolver,
    waterTrackProvider: WaterTrackProvider,
    heroProvider: HeroProvider,
    pedometerTrackProvider: PedometerTrackProvider,
    fastingTrackProvider: FastingTrackProvider,
    moodTrackProvider: MoodTrackProvider
) : ViewModel() {

    private val hero = heroProvider.requireHero().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val waterTracks = waterTrackProvider.provideWaterTracksByDayRange(
        LocalDate.now().createRangeForCurrentDay()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val isPedometerRunning = pedometerManager.isTracking.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    private val isPedometerAvailable = pedometerManager.availability.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    private val waterTrackingFeature = combine(
        waterTracks,
        hero.filterNotNull()
    ) { waterTrackList, hero ->
        val dailyRateInMilliliters = waterIntakeResolver.resolve(
            hero.weight,
            hero.exerciseStressTime,
            hero.gender
        )

        if (waterTrackList.isNotEmpty()) {
            val millilitersDrunk = waterTrackList.sumOf { waterTrack ->
                waterPercentageResolver.resolve(
                    waterTrack.drinkType,
                    waterTrack.millilitersCount
                )
            }

            DashboardItem.WaterTrackingFeature(
                millilitersDrunk = millilitersDrunk,
                dailyRateInMilliliters = dailyRateInMilliliters
            )
        } else {
            DashboardItem.WaterTrackingFeature(
                millilitersDrunk = null,
                dailyRateInMilliliters = dailyRateInMilliliters
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val pedometerFeature = combine(
        pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay()
        ),
        isPedometerRunning,
        isPedometerAvailable
    ) { pedometerTrackList, isPedometerRunning, availability ->
        val dailyRateInSteps = dailyRateStepsResolver.resolve()

        if (pedometerTrackList.isNotEmpty()) {
            val stepsWalked = pedometerTrackList.sumOf { it.stepsCount }

            DashboardItem.PedometerFeature(
                stepsWalked = stepsWalked,
                dailyRateInSteps = dailyRateInSteps,
                isPedometerRunning = isPedometerRunning,
                permissionsGranted = availability is Available
            )
        } else {
            DashboardItem.PedometerFeature(
                stepsWalked = null,
                dailyRateInSteps = dailyRateInSteps,
                isPedometerRunning = isPedometerRunning,
                permissionsGranted = availability is Available
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val fastingFeature = fastingTrackProvider.provideFastingTracksByStartTime(
        LocalDate.now().createRangeForCurrentDay()
    ).map { fastingTrackList ->
        if (fastingTrackList.isNotEmpty() && fastingTrackList[0].interruptedTime == null) {
            val currentFastingTrack = fastingTrackList[0]

            val hoursInPlan = fastingPlanDurationResolver.resolve(
                currentFastingTrack.fastingPlan,
                null
            )

            val hoursLeft = Clock.System.now() - currentFastingTrack.startTime

            DashboardItem.FastingFeature(
                hoursLeft = hoursLeft.inWholeHours.toInt(),
                hoursInPlan = hoursInPlan.toInt()
            )
        } else {
            DashboardItem.FastingFeature(
                hoursLeft = null,
                hoursInPlan = null
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
            pedometerManager.apply {
                if (isTracking.value) {
                    stopTracking()
                } else {
                    startTracking()
                }
            }
        }
    }

    data class State(
        val dashboardItems: List<DashboardItem>
    )
}