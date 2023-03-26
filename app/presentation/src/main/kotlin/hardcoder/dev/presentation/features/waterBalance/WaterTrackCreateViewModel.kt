package hardcoder.dev.presentation.features.waterBalance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.waterBalance.CorrectMillilitersInput
import hardcoder.dev.logic.features.waterBalance.MillilitersCount
import hardcoder.dev.logic.features.waterBalance.ValidatedMillilitersCount
import hardcoder.dev.logic.features.waterBalance.WaterIntakeResolver
import hardcoder.dev.logic.features.waterBalance.WaterTrackCreator
import hardcoder.dev.logic.features.waterBalance.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.hero.HeroProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class WaterTrackCreateViewModel(
    private val waterTrackCreator: WaterTrackCreator,
    drinkTypeProvider: DrinkTypeProvider,
    heroProvider: HeroProvider,
    private val waterIntakeResolver: WaterIntakeResolver,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
) : ViewModel() {

    private val drinkTypes = drinkTypeProvider.provideAllDrinkTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    private val selectedDrink = MutableStateFlow<DrinkType?>(null)
    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow<Int?>(null)
    private val hero = heroProvider.requireHero()
    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val validatedMillilitersCountState = millilitersDrunk.map {
        val currentHero = hero.first()
        it?.let {
            waterTrackMillilitersValidator.validate(
                data = MillilitersCount(it),
                dailyWaterIntakeInMillisCount = waterIntakeResolver.resolve(
                    currentHero.weight,
                    currentHero.exerciseStressTime,
                    currentHero.gender
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    init {
        viewModelScope.launch {
            selectedDrink.value = drinkTypes.value.firstOrNull()
        }
    }

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
            selectedDrink = null,
            validatedMillilitersCount = null,
            creationAllowed = false
        )
    )

    fun createWaterTrack() {
        viewModelScope.launch {
            val validatedMillilitersCount = validatedMillilitersCountState.value
            require(validatedMillilitersCount is CorrectMillilitersInput)
            val selectedDrink = requireNotNull(selectedDrink.value)

            waterTrackCreator.createWaterTrack(
                date = selectedDate.value.getStartOfDay(),
                millilitersCount = validatedMillilitersCount.data.value,
                drinkType = selectedDrink
            )
            creationState.value = CreationState.Executed
        }
    }

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    fun updateSelectedDrink(drink: DrinkType) {
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
        val drinks: List<DrinkType>,
        val selectedDrink: DrinkType?,
        val selectedDate: LocalDate
    )
}