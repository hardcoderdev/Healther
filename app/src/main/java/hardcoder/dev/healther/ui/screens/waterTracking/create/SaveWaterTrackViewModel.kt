package hardcoder.dev.healther.ui.screens.waterTracking.create

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.resolvers.WaterPercentageResolver
import hardcoder.dev.healther.logic.validators.CorrectMillilitersInput
import hardcoder.dev.healther.logic.validators.IncorrectMillilitersInput
import hardcoder.dev.healther.logic.validators.MillilitersCount
import hardcoder.dev.healther.logic.validators.WaterTrackMillilitersValidator
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
    private val waterPercentageResolver: WaterPercentageResolver,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))
    private val drinkTypes = waterTrackRepository.getAllDrinkTypes()
    private val validationState = MutableStateFlow<ValidationState>(ValidationState.NotValidated)

    val state = combine(
        selectedDate,
        millilitersDrunk,
        drinkTypes,
        selectedDrink,
        validationState
    ) { selectedDate, millilitersDrunk, drinkTypes, selectedDrink, validationState ->
        State(
            selectedDate = selectedDate,
            millilitersCount = millilitersDrunk,
            drinks = drinkTypes,
            selectedDrink = selectedDrink,
            validationState = validationState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            selectedDate = selectedDate.value,
            millilitersCount = millilitersDrunk.value,
            drinks = emptyList(),
            selectedDrink = selectedDrink.value,
            validationState = ValidationState.NotValidated
        )
    )

    fun createWaterTrack() = viewModelScope.launch {
        val validationResult = waterTrackMillilitersValidator.validate(
            data = MillilitersCount(millilitersDrunk.value),
            dailyWaterIntakeInMillisCount = waterPercentageResolver.resolve(
                selectedDrink.value.type,
                millilitersDrunk.value
            )
        )

        when (validationResult) {
            is CorrectMillilitersInput -> {
                waterTrackRepository.insert(
                    WaterTrack(
                        date = selectedDate.value.getStartOfDay(),
                        millilitersCount = millilitersDrunk.value,
                        drinkType = selectedDrink.value.type
                    )
                )
            }

            is IncorrectMillilitersInput -> {
                validationState.value = ValidationState.Error(validationResult.reason)
            }
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

    sealed class ValidationState {
        object NotValidated : ValidationState()
        object Success : ValidationState()
        data class Error(@StringRes val reasonResId: Int) : ValidationState()
    }

    data class State(
        val validationState: ValidationState,
        val selectedDate: LocalDate,
        val millilitersCount: Int,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
    )
}