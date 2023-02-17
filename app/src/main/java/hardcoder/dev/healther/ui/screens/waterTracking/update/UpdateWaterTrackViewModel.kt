package hardcoder.dev.healther.ui.screens.waterTracking.update

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.logic.resolvers.DrinkTypeImageResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class UpdateWaterTrackViewModel(
    private val waterTrackId: Int,
    private val waterTrackRepository: WaterTrackRepository,
    private val drinkTypeImageResolver: DrinkTypeImageResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))
    private val drinkTypes = waterTrackRepository.getAllDrinkTypes()

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        selectedDate,
        millilitersDrunk,
        drinkTypes,
        selectedDrink
    ) { selectedDate, millilitersDrunk, drinkTypes, selectedDrink ->
        State(
            selectedDate = selectedDate,
            millilitersCount = millilitersDrunk,
            drinks = drinkTypes,
            selectedDrink = selectedDrink
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            selectedDate = selectedDate.value,
            millilitersCount = millilitersDrunk.value,
            drinks = emptyList(),
            selectedDrink = selectedDrink.value
        )
    )

    fun updateWaterTrack() = viewModelScope.launch {
        waterTrackRepository.getWaterTrackById(waterTrackId).firstOrNull()?.let {
            val updatedTrack = it.copy(
                time = selectedDate.value.getStartOfDay(),
                millilitersCount = millilitersDrunk.value,
                drinkType = selectedDrink.value.type
            )
            waterTrackRepository.update(updatedTrack)
        } ?: throw NotFoundException("Track not found by it's id")
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
        val selectedDate: LocalDate,
        val millilitersCount: Int,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
    )
}