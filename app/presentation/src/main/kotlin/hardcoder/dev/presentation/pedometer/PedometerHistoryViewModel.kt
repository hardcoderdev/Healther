package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.mapItems
import hardcoder.dev.logic.pedometer.PedometerTrackDeleter
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val pedometerTrackDeleter: PedometerTrackDeleter
) : ViewModel() {

    private val selectedRangeStateFlow =
        MutableStateFlow(LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault()))
    val state = selectedRangeStateFlow.flatMapLatest { range ->
        pedometerTrackProvider.providePedometerTracksByRange(range)
    }.mapItems { it.toItem() }.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            pedometerTrackItems = emptyList()
        )
    )

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    fun deleteTrack(pedometerTrackId: Int) {
        viewModelScope.launch {
            pedometerTrackDeleter.deleteById(pedometerTrackId)
        }
    }

    data class State(val pedometerTrackItems: List<PedometerTrackItem>)

}