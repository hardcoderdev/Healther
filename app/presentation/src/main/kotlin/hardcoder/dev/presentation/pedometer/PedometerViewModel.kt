package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.millisToLocalDateTime
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.MinutesResolver
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
    private val caloriesResolver: CaloriesResolver,
    private val minutesResolver: MinutesResolver
) : ViewModel() {

    private val isTrackingNow = MutableStateFlow(false)
    private val totalWastedTime = MutableStateFlow(0L)
    private val totalKilometersCount = MutableStateFlow(0.0f)
    private val totalCaloriesCount = MutableStateFlow(0.0f)
    private val chartEntries = MutableStateFlow(listOf(0 to 0))
    private val dailyRateStepsCount = MutableStateFlow(DAILY_RATE_PEDOMETER)

    private val totalStepsCount = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    ).map { pedometerTracks ->
        val totalStepsCount = pedometerTracks.sumOf { it.stepsCount }

        totalCaloriesCount.value = caloriesResolver.resolve(totalStepsCount)
        totalKilometersCount.value = kilometersResolver.resolve(totalStepsCount)
        chartEntries.value = pedometerTracks.groupBy {
            it.range.first.millisToLocalDateTime().hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.stepsCount }
        }

        totalWastedTime.value = pedometerTracks.groupBy {
            it.range.first.millisToLocalDateTime().hour
        }.map { entry ->
            entry.value.sumOf { it.range.first + it.range.last }
        }.sum()

        totalStepsCount
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    val state = combine(
        isTrackingNow,
        totalStepsCount,
        totalKilometersCount,
        totalCaloriesCount,
        totalWastedTime,
        dailyRateStepsCount,
        chartEntries
    ) { isTrackingNow, totalStepsCount, totalKilometersCount, totalCaloriesCount,
        totalWastedTime, dailyRateStepsCount, chartEntries ->
        LoadingState.Loaded(
            State(
                isTrackingNow,
                totalStepsCount,
                totalKilometersCount,
                totalCaloriesCount,
                totalWastedTime,
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
        val totalWastedTime: Long,
        val dailyRateStepsCount: Int,
        val chartEntries: List<Pair<Int, Int>>
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}