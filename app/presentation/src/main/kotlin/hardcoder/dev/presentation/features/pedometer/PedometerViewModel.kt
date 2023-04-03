package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.logic.entities.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PedometerViewModel(
    private val pedometerManager: PedometerManager,
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider
) : ViewModel() {

    private val dailyRateStepsCount = MutableStateFlow(DAILY_RATE_PEDOMETER)

    private val pedometerStatistic = pedometerStatisticProvider.providePedometerStatistic().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val tracks = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val totalStepCount = tracks.map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val totalTrackingTime = tracks.map { pedometerTracks ->
        pedometerTracks.sumOf { it.range.endInclusive.toEpochMilliseconds() - it.range.start.toEpochMilliseconds() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0L
    )

    private val totalCaloriesCount = totalStepCount.map {
        caloriesResolver.resolve(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    private val totalKilometersCount = totalStepCount.map {
        kilometersResolver.resolve(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    private val chartEntries = tracks.map { pedometerTracks ->
        pedometerTracks.groupBy {
            it.range.start.toLocalDateTime(TimeZone.currentSystemDefault()).hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.stepsCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = listOf(0 to 0)
    )

    private val initialPermissionsScreenShowed = MutableStateFlow(false)

    val state = combine(
        pedometerManager.isTracking,
        totalStepCount,
        totalKilometersCount,
        totalCaloriesCount,
        totalTrackingTime,
        dailyRateStepsCount,
        chartEntries,
        pedometerStatistic,
        pedometerManager.availability,
        initialPermissionsScreenShowed
    ) { isTrackingNow, totalStepsCount, totalKilometersCount, totalCaloriesCount,
        totalTrackingTime, dailyRateStepsCount, chartEntries, pedometerStatistic,
        availability, initialPermissionsScreenShowed ->
        LoadingState.Loaded(
            State(
                isTrackingNow,
                totalStepsCount,
                totalKilometersCount,
                totalCaloriesCount,
                totalTrackingTime,
                dailyRateStepsCount,
                chartEntries,
                pedometerStatistic,
                availability,
                initialPermissionsScreenShowed
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingState.Loading
    )

    fun togglePedometerTracking() {
        viewModelScope.launch {
            pedometerManager.apply {
                requestBattery()
                requestPermissions()
                initialPermissionsScreenShowed.value = true
                if (isTracking.value) {
                    stopTracking()
                } else {
                    startTracking()
                }
            }
        }
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }

    data class State(
        val isTrackingNow: Boolean,
        val totalStepsCount: Int,
        val totalKilometersCount: Float,
        val totalCaloriesBurned: Float,
        val totalTrackingTime: Long,
        val dailyRateStepsCount: Int,
        val chartEntries: List<Pair<Int, Int>>,
        val pedometerStatistic: PedometerStatistic?,
        val availability: PedometerManager.Availability,
        val initialPermissionScreenShowed: Boolean
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}