package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.millisToLocalDateTime
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val pedometerTrackProvider: PedometerTrackProvider
) : ViewModel() {

    private val selectedRangeStateFlow =
        MutableStateFlow(LocalDate.now().createRangeForCurrentDay())
    private val pedometerTracks = selectedRangeStateFlow.flatMapLatest { range ->
        pedometerTrackProvider.providePedometerTracksByRange(range)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val totalStepsCount = pedometerTracks.map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val totalTrackingTime = pedometerTracks.map { pedometerTracks ->
        pedometerTracks.sumOf { it.range.last - it.range.first }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val totalCaloriesCount = totalStepsCount.map {
        caloriesResolver.resolve(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    private val totalKilometersCount = totalStepsCount.map {
        kilometersResolver.resolve(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    private val chartEntries = pedometerTracks.map { pedometerTracks ->
        pedometerTracks.groupBy {
            it.range.first.millisToLocalDateTime().hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.stepsCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        totalStepsCount,
        totalKilometersCount,
        totalCaloriesCount,
        totalTrackingTime,
        chartEntries
    ) { totalStepsCount, totalKilometersCount, totalCaloriesCount, totalTrackingTime, chartEntries ->
        State(
            totalStepsCount = totalStepsCount,
            totalKilometersCount = totalKilometersCount,
            totalCaloriesBurned = totalCaloriesCount,
            totalTrackingTime = totalTrackingTime,
            chartEntries = chartEntries
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            totalStepsCount = totalStepsCount.value,
            totalKilometersCount = totalKilometersCount.value,
            totalCaloriesBurned = totalCaloriesCount.value,
            totalTrackingTime = totalTrackingTime.value,
            chartEntries = chartEntries.value
        )
    )

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    data class State(
        val totalStepsCount: Int,
        val totalKilometersCount: Float,
        val totalCaloriesBurned: Float,
        val totalTrackingTime: Long,
        val chartEntries: List<Pair<Int, Int>>
    )
}