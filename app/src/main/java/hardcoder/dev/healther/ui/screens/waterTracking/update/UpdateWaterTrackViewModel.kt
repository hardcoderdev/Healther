package hardcoder.dev.healther.ui.screens.waterTracking.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.logic.DrinkTypeImageResolver
import hardcoder.dev.healther.logic.WaterPercentageResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class UpdateWaterTrackViewModel(
    private val waterTrackId: Int,
    private val waterTrackRepository: WaterTrackRepository,
    private val drinkTypeImageResolver: DrinkTypeImageResolver,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val resolvedMillilitersDrunk = MutableStateFlow(0)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        millilitersDrunk,
        selectedDrink
    ) { millisDrunk, selectDrink ->
        State(
            millilitersCount = millisDrunk,
            drinks = waterTrackRepository.getAllDrinkTypes(),
            selectedDrink = selectDrink
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            millilitersCount = 0,
            drinks = waterTrackRepository.getAllDrinkTypes(),
            selectedDrink = selectedDrink.value
        )
    )

    fun updateWaterTrack() = viewModelScope.launch {
        waterTrackRepository.getWaterTrackById(waterTrackId)?.let {
            resolvedMillilitersDrunk.value = waterPercentageResolver.resolve(
                drinkType = selectedDrink.value.type,
                millilitersDrunk = millilitersDrunk.value
            )

            val updatedTrack = it.first().copy(
                time = selectedDate.value.getStartOfDay(),
                millilitersCount = resolvedMillilitersDrunk.value,
                drinkType = selectedDrink.value.type
            )

            waterTrackRepository.update(updatedTrack)
        }
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

    private fun fillStateWithUpdatedTrack() = viewModelScope.launch {
        waterTrackRepository.getWaterTrackById(waterTrackId).firstOrNull()?.let { waterTrack ->
            millilitersDrunk.value = waterTrack.millilitersCount
            selectedDrink.value = Drink(
                type = waterTrack.drinkType,
                image = drinkTypeImageResolver.resolve(waterTrack.drinkType)
            )
        }
    }

    data class State(
        val millilitersCount: Int,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
    )
}