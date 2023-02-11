package hardcoder.dev.healther.ui.screens.waterTracking.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.WaterPercentageResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class SaveWaterTrackViewModel(
    private val waterTrackId: Int = -1,
    private val waterTrackRepository: WaterTrackRepository,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val resolvedMillilitersDrunk = MutableStateFlow(0)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))

    val state = combine(
        millilitersDrunk,
        selectedDrink
    ) { millilitersDrunk, selectedDrink ->
        State.Input(
            millilitersCount = millilitersDrunk,
            drinks = waterTrackRepository.getAllDrinkTypes(),
            selectedDrink = selectedDrink,
            day = LocalDate.now(),
            time = LocalTime.now()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Input(
            millilitersCount = 0,
            drinks = waterTrackRepository.getAllDrinkTypes(),
            selectedDrink = selectedDrink.value,
            day = LocalDate.now(),
            time = LocalTime.now()
        )
    )

    fun createWaterTrack() = viewModelScope.launch {
        resolvedMillilitersDrunk.value = waterPercentageResolver.resolve(
            drinkType = selectedDrink.value.type,
            millilitersDrunk = millilitersDrunk.value
        )

        waterTrackRepository.insert(
            WaterTrack(
                time = selectedDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli(),
                millilitersCount = resolvedMillilitersDrunk.value,
                drinkType = selectedDrink.value.type
            )
        )
    }

    fun updateWaterTrack() = viewModelScope.launch {
        val waterTrack = getWaterTrackById().await()
        waterTrack.copy(
            time = selectedDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli(),
            millilitersCount = resolvedMillilitersDrunk.value,
            drinkType = selectedDrink.value.type
        )

        waterTrackRepository.update(waterTrack)
    }

    fun getWaterTrackById() = viewModelScope.async {
        return@async waterTrackRepository.getWaterTrackById(waterTrackId)
    }

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    fun updateSelectedDrink(drink: Drink) {
        selectedDrink.value = drink
    }

    fun updateSelectedDate(localDate: LocalDate) {
        selectedDate.value = localDate
    }

    sealed class State {
        data class Input(
            val millilitersCount: Int,
            val day: LocalDate,
            val time: LocalTime,
            val drinks: List<Drink>,
            val selectedDrink: Drink,
        ) : State()
    }
}