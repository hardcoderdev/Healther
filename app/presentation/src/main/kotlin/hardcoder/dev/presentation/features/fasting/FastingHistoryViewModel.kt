package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.fasting.FastingTrack
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class FastingHistoryViewModel(
    private val fastingTrackProvider: FastingTrackProvider
) : ViewModel() {

    private val selectedDayStateFlow = MutableStateFlow(
        LocalDate.now().getStartOfDay()..LocalDate.now().getEndOfDay()
    )

    val state = selectedDayStateFlow.flatMapLatest { dayRange ->
        fastingTrackProvider.provideFastingTracksByStartTime(dayRange)
    }.map {
        State(fastingTracks = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            fastingTracks = emptyList()
        )
    )

    fun selectDay(localDate: LocalDate) {
        selectedDayStateFlow.value = localDate.getStartOfDay()..localDate.getEndOfDay()
    }

    data class State(val fastingTracks: List<FastingTrack>)
}