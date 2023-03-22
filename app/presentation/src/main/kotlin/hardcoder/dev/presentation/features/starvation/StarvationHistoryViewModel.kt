package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationTrack
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class StarvationHistoryViewModel(
    private val starvationTrackProvider: StarvationTrackProvider
) : ViewModel() {

    private val selectedDayStateFlow = MutableStateFlow(
        LocalDate.now().getStartOfDay()..LocalDate.now().getEndOfDay()
    )

    val state = selectedDayStateFlow.flatMapLatest { dayRange ->
        starvationTrackProvider.provideStarvationTracksByStartTime(dayRange)
    }.map {
        State(starvationTracks = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            starvationTracks = emptyList()
        )
    )

    fun selectDay(localDate: LocalDate) {
        selectedDayStateFlow.value = localDate.getStartOfDay()..localDate.getEndOfDay()
    }

    data class State(
        val starvationTracks: List<StarvationTrack>
    )
}