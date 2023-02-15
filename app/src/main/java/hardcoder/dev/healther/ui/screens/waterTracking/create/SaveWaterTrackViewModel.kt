package hardcoder.dev.healther.ui.screens.waterTracking.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.WaterPercentageResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class SaveWaterTrackViewModel(
    private val waterTrackRepository: WaterTrackRepository,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val selectedDrink = MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))
    private val drinkTypes = waterTrackRepository.getAllDrinkTypes()

    val state = combine(
        millilitersDrunk,
        drinkTypes,
        selectedDrink
    ) { millilitersDrunk, drinkTypes, selectedDrink ->
        State(
            millilitersCount = millilitersDrunk,
            drinks = drinkTypes,
            selectedDrink = selectedDrink
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            millilitersCount = millilitersDrunk.value,
            drinks = emptyList(),
            selectedDrink = selectedDrink.value
        )
    )

    fun createWaterTrack() = viewModelScope.launch {
        waterTrackRepository.insert(
            WaterTrack(
                time = selectedDate.value.getStartOfDay(),
                millilitersCount = millilitersDrunk.value,
                drinkType = selectedDrink.value.type
            )
        )
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

    data class State(
        val millilitersCount: Int,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
    )
}