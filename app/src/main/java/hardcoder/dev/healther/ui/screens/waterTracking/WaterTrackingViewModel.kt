package hardcoder.dev.healther.ui.screens.waterTracking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.WaterIntakeResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.utils.DateUtils
import hardcoder.dev.healther.ui.screens.welcome.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class WaterTrackingViewModel(
    private val waterTrackRepository: WaterTrackRepository,
    private val waterIntakeResolver: WaterIntakeResolver
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val dailyWaterIntake = MutableStateFlow(0)
    private val waterTracksList = MutableStateFlow<List<WaterTrack>>(emptyList())

    val state = combine(
        millilitersDrunk,
        waterTracksList,
        dailyWaterIntake
    ) { millilitersDrunk, waterTracks, dailyWaterIntake ->
        State.Drunk(
            millisCount = millilitersDrunk,
            waterTracks = waterTracks,
            dailyWaterIntake = dailyWaterIntake
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Drunk(
            millisCount = 0,
            waterTracks = emptyList(),
            dailyWaterIntake = 0
        )
    )

    fun fetchWaterTracks() = viewModelScope.launch {
        val startOfCurrentDay = DateUtils.getStartOfDay(LocalDate.now())
        val endOfCurrentDay = DateUtils.getEndOfDay(LocalDate.now())
        waterTrackRepository.getAllWaterTracks(startOfCurrentDay, endOfCurrentDay)
            .collectLatest { waterTracks ->
                val millilitersCount = waterTracks.sumOf { it.millilitersCount }
                Log.d("dwdwd", millilitersCount.toString())
                millilitersDrunk.value = millilitersCount
                waterTracksList.value = waterTracks
            }
    }

    fun delete(waterTrack: WaterTrack) = viewModelScope.launch {
        waterTrackRepository.delete(waterTrack)
    }

    fun resolveDailyWaterIntake(weight: Int, stressTime: Int, gender: Gender) {
        dailyWaterIntake.value = waterIntakeResolver.resolve(weight, stressTime, gender)
    }

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    sealed class State {
        data class Drunk(
            val millisCount: Int,
            val dailyWaterIntake: Int,
            val waterTracks: List<WaterTrack>
        ) : State()

        class Completed : State()
    }
}