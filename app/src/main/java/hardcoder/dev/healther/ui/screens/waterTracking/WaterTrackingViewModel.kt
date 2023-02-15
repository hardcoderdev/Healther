package hardcoder.dev.healther.ui.screens.waterTracking

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.logic.WaterIntakeResolver
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.extensions.getEndOfDay
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import hardcoder.dev.healther.ui.base.flow.combine
import hardcoder.dev.healther.ui.screens.setUpFlow.gender.Gender
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

data class WaterTrackItem(
    val id: Int,
    @StringRes val drinkNameResId: Int,
    @DrawableRes val imageResId: Int,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long
)

class WaterTrackingViewModel(
    private val waterTrackRepository: WaterTrackRepository,
    private val userRepository: UserRepository,
    private val waterIntakeResolver: WaterIntakeResolver
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val dailyWaterIntake = MutableStateFlow(0)
    private val waterTracksList = MutableStateFlow<List<WaterTrackItem>>(emptyList())
    private val weight = userRepository.weight.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )
    private val exerciseStressTime = userRepository.exerciseStressTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )
    private val gender = userRepository.gender.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Gender.MALE
    )

    val state = combine(
        millilitersDrunk,
        waterTracksList,
        dailyWaterIntake,
        weight,
        exerciseStressTime,
        gender
    ) { millilitersDrunk, waterTracks, dailyWaterIntake, weight, exerciseStressTime, gender ->
        LoadingState.Loaded(
            State(
                millisCount = millilitersDrunk,
                waterTracks = waterTracks,
                dailyWaterIntake = dailyWaterIntake,
                weight = weight,
                exerciseStressTime = exerciseStressTime,
                gender = gender
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingState.Loading
    )

    init {
        updateFirstLaunch()
        resolveDailyWaterIntake()
        fetchWaterTracks()
    }

    private fun fetchWaterTracks() = viewModelScope.launch {
        val currentDay = LocalDate.now()
        val startOfCurrentDay = currentDay.getStartOfDay()
        val endOfCurrentDay = currentDay.getEndOfDay()
        waterTrackRepository.getWaterTracksByDayRange(startOfCurrentDay..endOfCurrentDay)
            .collectLatest { waterTracks ->
                val millilitersCount = waterTracks.sumOf { it.millilitersCount }
                millilitersDrunk.value = millilitersCount
                waterTracksList.value = waterTracks
            }
    }

    private fun updateFirstLaunch() = viewModelScope.launch {
        userRepository.updateFirstLaunch(isFirstLaunch = false)
    }

    private fun resolveDailyWaterIntake() {
        dailyWaterIntake.value = waterIntakeResolver.resolve(
            weight.value,
            exerciseStressTime.value,
            gender.value
        )
    }

    fun delete(waterTrackId: Int) = viewModelScope.launch(Dispatchers.IO) {
        waterTrackRepository.delete(waterTrackId)
    }

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    data class State(
        val weight: Int,
        val exerciseStressTime: Int,
        val gender: Gender,
        val millisCount: Int,
        val dailyWaterIntake: Int,
        val waterTracks: List<WaterTrackItem>
    )

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }
}