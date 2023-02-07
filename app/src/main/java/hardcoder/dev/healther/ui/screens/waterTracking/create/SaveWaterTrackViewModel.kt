package hardcoder.dev.healther.ui.screens.waterTracking.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.data.local.room.WaterTrackDao
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.WaterPercentageResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SaveWaterTrackViewModel(
    private val waterTrackDao: WaterTrackDao,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val resolvedMillilitersDrunk = MutableStateFlow(0)
    private val drinkType = MutableStateFlow(DrinkType.WATER)

    val state = combine(
        millilitersDrunk,
        drinkType
    ) { millilitersDrunk, drinkType ->
        State.Input(
            millilitersCount = millilitersDrunk,
            drinkType = drinkType,
            time = System.currentTimeMillis()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Input(
            millilitersCount = 0,
            drinkType = DrinkType.WATER,
            time = 0
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

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    fun updateDrinkType(type: DrinkType) {
        drinkType.value = type
    }

    sealed class State {
        data class Input(
            val millilitersCount: Int,
            val drinkType: DrinkType,
            val time: Long = System.currentTimeMillis()
        ) : State()
    }
}