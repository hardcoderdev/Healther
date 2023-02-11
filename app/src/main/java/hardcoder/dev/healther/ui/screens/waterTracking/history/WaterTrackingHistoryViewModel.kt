package hardcoder.dev.healther.ui.screens.waterTracking.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

class WaterTrackingHistoryViewModel(
    private val waterTrackRepository: WaterTrackRepository
) : ViewModel() {

    private val _state = MutableStateFlow<List<WaterTrack>>(emptyList())
    val state: StateFlow<List<WaterTrack>> = _state.asStateFlow()

    fun fetchWaterTracks(selectedDay: LocalDate) = viewModelScope.launch {
        val javaLocalDate = selectedDay.toJavaLocalDate()
        val startOfDay = DateUtils.getStartOfDay(javaLocalDate)
        val endOfDay = DateUtils.getEndOfDay(javaLocalDate)
        waterTrackRepository.getAllWaterTracks(startOfDay, endOfDay)
            .collectLatest { waterTracks ->
                _state.value = waterTracks
            }
    }

    fun deleteTrack(waterTrack: WaterTrack) = viewModelScope.launch(Dispatchers.IO) {
        waterTrackRepository.delete(waterTrack)
    }
}