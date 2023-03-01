package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.mapItems
import hardcoder.dev.extensions.millisToLocalDateTime
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val pedometerTrackProvider: PedometerTrackProvider
) : ViewModel() {

    private val selectedRangeStateFlow = MutableStateFlow(LocalDate.now().createRangeForCurrentDay())
    private val chartEntries = MutableStateFlow(listOf(0 to 0))

    val state = selectedRangeStateFlow.flatMapLatest { range ->
        pedometerTrackProvider.providePedometerTracksByRange(range)
    }.onEach { pedometerTracks ->
        chartEntries.value = pedometerTracks.groupBy {
            it.range.first.millisToLocalDateTime().hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.stepsCount }
        }
    }.mapItems {
        it.toItem(
            kilometersCount = kilometersResolver.resolve(it.stepsCount),
            caloriesBurnt = caloriesResolver.resolve(it.stepsCount)
        )
    }.map { pedometerTrackItems ->
        val stepsCount = pedometerTrackItems.sumOf { it.stepsCount }
            State(
                chartEntries = chartEntries.value,
                pedometerTrackItem = if (stepsCount != 0) {
                    PedometerTrackItem(
                        stepsCount = stepsCount,
                        kilometersCount = kilometersResolver.resolve(stepsCount),
                        caloriesBurnt = caloriesResolver.resolve(stepsCount)
                    )
                } else {
                    null
                }
            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            pedometerTrackItem = null,
            chartEntries = chartEntries.value
        )
    )

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    data class State(
        val pedometerTrackItem: PedometerTrackItem?,
        val chartEntries: List<Pair<Int, Int>>
    )
}