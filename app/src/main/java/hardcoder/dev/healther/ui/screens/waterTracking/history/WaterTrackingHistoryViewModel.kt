package hardcoder.dev.healther.ui.screens.waterTracking.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.extensions.createRangeForCurrentDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class WaterTrackingHistoryViewModel(
    private val waterTrackRepository: WaterTrackRepository
) : ViewModel() {

    private val selectedRangeStateFlow =
        MutableStateFlow(LocalDate.now().createRangeForCurrentDay())
    val state = selectedRangeStateFlow.flatMapLatest { range ->
        waterTrackRepository.getAllWaterTracks(range.first, range.last)
    }.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            drinks = emptyList()
        )
    )

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    fun deleteTrack(waterTrack: WaterTrack) = viewModelScope.launch(Dispatchers.IO) {
        waterTrackRepository.delete(waterTrack)
    }

    data class State(val drinks: List<WaterTrack>)
}