package hardcoder.dev.healther.ui.screens.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.data.local.room.WaterTrackDao
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.WaterIntakeResolver
import hardcoder.dev.healther.logic.WaterPercentageResolver
import hardcoder.dev.healther.ui.screens.welcome.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterTrackingViewModel(
    private val waterTrackDao: WaterTrackDao,
    private val waterPercentageResolver: WaterPercentageResolver,
    private val waterIntakeResolver: WaterIntakeResolver
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val resolvedMillilitersDrunk = MutableStateFlow(0)
    private val drinkType = MutableStateFlow(DrinkType.WATER)
    private val dailyWaterIntake = MutableStateFlow(0)

    val state = combine(
        millilitersDrunk,
        drinkType,
        dailyWaterIntake
    ) { millilitersDrunk, drinkType, dailyWaterIntake ->
        State.Drunk(millisCount = millilitersDrunk, dailyWaterIntake = dailyWaterIntake)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Drunk(
            millisCount = 0,
            dailyWaterIntake = 0
        )
    )

    fun createWaterTrack() = viewModelScope.launch {
        resolvedMillilitersDrunk.value = waterPercentageResolver.resolve(
            drinkType = drinkType.value,
            millilitersDrunk = millilitersDrunk.value
        )

        waterTrackDao.insert(
            WaterTrack(
                time = System.currentTimeMillis(),
                millilitersCount = resolvedMillilitersDrunk.value,
                drinkType = drinkType.value
            )
        )
    }

    fun resolveDailyWaterIntake(weight: Int, stressTime: Int, gender: Gender) {
        dailyWaterIntake.value = waterIntakeResolver.resolve(weight, stressTime, gender)
    }

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    fun updateDrinkType(type: DrinkType) {
        drinkType.value = type
    }

    sealed class State {
        data class Drunk(val millisCount: Int, val dailyWaterIntake: Int) : State()
        class Completed : State()
    }
}