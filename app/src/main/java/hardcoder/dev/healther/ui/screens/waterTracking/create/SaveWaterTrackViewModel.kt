package hardcoder.dev.healther.ui.screens.waterTracking.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.resolvers.WaterIntakeResolver
import hardcoder.dev.healther.logic.validators.CorrectMillilitersInput
import hardcoder.dev.healther.logic.validators.MillilitersCount
import hardcoder.dev.healther.logic.validators.ValidatedMillilitersCount
import hardcoder.dev.healther.logic.validators.WaterTrackMillilitersValidator
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import hardcoder.dev.healther.ui.base.flow.combine
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class SaveWaterTrackViewModel(
    private val waterTrackRepository: WaterTrackRepository,
    private val userRepository: UserRepository,
    private val waterIntakeResolver: WaterIntakeResolver,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow<Int?>(null)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))
    private val drinkTypes = waterTrackRepository.getAllDrinkTypes()
    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val validatedMillilitersCountState = millilitersDrunk.map {
        it?.let {
            waterTrackMillilitersValidator.validate(
                data = MillilitersCount(it),
                dailyWaterIntakeInMillisCount = waterIntakeResolver.resolve(
                    userRepository.weight.first(),
                    userRepository.exerciseStressTime.first(),
                    userRepository.gender.first()
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        creationState,
        selectedDate,
        millilitersDrunk,
        drinkTypes,
        selectedDrink,
        validatedMillilitersCountState
    ) { creationState, selectedDate, millilitersDrunk, drinkTypes, selectedDrink, validatedMillilitersCountState ->
        State(
            creationState = creationState,
            selectedDate = selectedDate,
            millilitersCount = millilitersDrunk,
            drinks = drinkTypes,
            selectedDrink = selectedDrink,
            validatedMillilitersCount = validatedMillilitersCountState,
            creationAllowed = validatedMillilitersCountState is CorrectMillilitersInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationState = creationState.value,
            selectedDate = selectedDate.value,
            millilitersCount = millilitersDrunk.value,
            drinks = emptyList(),
            selectedDrink = selectedDrink.value,
            validatedMillilitersCount = null,
            creationAllowed = false
        )
    )

    fun createWaterTrack() = viewModelScope.launch {
        require(validatedMillilitersCountState.value is CorrectMillilitersInput)
        waterTrackRepository.insert(
            WaterTrack(
                date = selectedDate.value.getStartOfDay(),
                millilitersCount = millilitersDrunk.value!!,
                drinkType = selectedDrink.value.type
            )
        )
        creationState.value = CreationState.Executed
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

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }

    data class State(
        val creationAllowed: Boolean,
        val creationState: CreationState,
        val millilitersCount: Int?,
        val validatedMillilitersCount: ValidatedMillilitersCount?,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
        val selectedDate: LocalDate
    )
}