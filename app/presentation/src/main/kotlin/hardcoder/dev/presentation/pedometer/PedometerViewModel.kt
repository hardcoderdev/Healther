package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.millisToLocalDateTime
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class PedometerViewModel(
    pedometerTrackProvider: PedometerTrackProvider,
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver
) : ViewModel() {

    private val isTrackingNow = MutableStateFlow(false)
    private val dailyRateStepsCount = MutableStateFlow(DAILY_RATE_PEDOMETER)

    private val tracks = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val totalStepCount = tracks.map {
        it.sumOf { it.stepsCount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val totalTrackingTime = tracks.map {
        it.sumOf { it.range.last - it.range.first }
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

    private val chartEntries = tracks.map {
        it.groupBy {
            it.range.first.millisToLocalDateTime().hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.stepsCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = listOf(0 to 0)
    )


    val state = combine(
        isTrackingNow,
        totalStepCount,
        totalKilometersCount,
        totalCaloriesCount,
        totalTrackingTime,
        dailyRateStepsCount,
        chartEntries
    ) { isTrackingNow, totalStepsCount, totalKilometersCount, totalCaloriesCount,
        totalTrackingTime, dailyRateStepsCount, chartEntries ->
        LoadingState.Loaded(
            State(
                isTrackingNow,
                totalStepsCount,
                totalKilometersCount,
                totalCaloriesCount,
                totalTrackingTime,
                dailyRateStepsCount,
                chartEntries
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingState.Loading
    )

    fun updateTrackingStatus(isTracking: Boolean) {
        isTrackingNow.value = isTracking
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
        val chartEntries: List<Pair<Int, Int>>
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}