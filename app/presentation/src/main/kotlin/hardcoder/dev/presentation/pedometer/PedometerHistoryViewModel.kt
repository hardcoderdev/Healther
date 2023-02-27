package hardcoder.dev.presentation.pedometer

import android.util.Log
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val pedometerTrackProvider: PedometerTrackProvider
) : ViewModel() {

    private val selectedRangeStateFlow = MutableStateFlow(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    )
    val state = selectedRangeStateFlow.flatMapLatest { range ->
        Log.d(
            "dwdwdw",
            range.first.toString() + " dwkdd" + range.last.toString()
        )
        pedometerTrackProvider.providePedometerTracksByRange(range)
    }.mapItems {
        it.toItem(
            kilometersCount = kilometersResolver.resolve(it.stepsCount),
            caloriesBurnt = caloriesResolver.resolve(it.stepsCount)
        )
    }.map { pedometerTrackItems ->
        val stepsCount = pedometerTrackItems.sumOf { it.stepsCount }
        State(
            PedometerTrackItem(
                range = pedometerTrackItems[0].range, // TODO MAYBE DELETE ?
                stepsCount = stepsCount,
                kilometersCount = kilometersResolver.resolve(stepsCount),
                caloriesBurnt = caloriesResolver.resolve(stepsCount)
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            pedometerTrackItem = null
        )
    )

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    data class State(val pedometerTrackItem: PedometerTrackItem?)
}