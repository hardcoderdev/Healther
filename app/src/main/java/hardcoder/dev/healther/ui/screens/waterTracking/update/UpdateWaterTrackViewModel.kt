package hardcoder.dev.healther.ui.screens.waterTracking.update

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.R
import hardcoder.dev.healther.entities.DrinkType
import hardcoder.dev.healther.logic.providers.HeroProvider
import hardcoder.dev.healther.logic.providers.WaterTrackProvider
import hardcoder.dev.healther.logic.resolvers.DrinkTypeImageResolver
import hardcoder.dev.healther.logic.resolvers.WaterIntakeResolver
import hardcoder.dev.healther.logic.updaters.WaterTrackUpdater
import hardcoder.dev.healther.logic.validators.CorrectMillilitersInput
import hardcoder.dev.healther.logic.validators.MillilitersCount
import hardcoder.dev.healther.logic.validators.ValidatedMillilitersCount
import hardcoder.dev.healther.logic.validators.WaterTrackMillilitersValidator
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import hardcoder.dev.healther.ui.base.flow.combine
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class UpdateWaterTrackViewModel(
    private val waterTrackId: Int,
    heroProvider: HeroProvider,
    private val waterTrackUpdater: WaterTrackUpdater,
    private val waterTrackProvider: WaterTrackProvider,
    private val drinkTypeImageResolver: DrinkTypeImageResolver,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    private val waterIntakeResolver: WaterIntakeResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val selectedDrink =
        MutableStateFlow(Drink(type = DrinkType.WATER, image = R.drawable.water))
    private val drinkTypes = waterTrackProvider.getAllDrinkTypes()
    private val hero = heroProvider.requireHero()
    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val validatedMillilitersCountState = millilitersDrunk.map {
        val currentHero = hero.first()
        waterTrackMillilitersValidator.validate(
            data = MillilitersCount(it),
            dailyWaterIntakeInMillisCount = waterIntakeResolver.resolve(
                currentHero.weight,
                currentHero.exerciseStressTime,
                currentHero.gender
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        selectedDate,
        millilitersDrunk,
        drinkTypes,
        selectedDrink,
        validatedMillilitersCountState
    ) { updateState, selectedDate, millilitersDrunk, drinkTypes, selectedDrink, validatedMillilitersCountState ->
        State(
            updateState = updateState,
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
            selectedDate = selectedDate.value,
            millilitersCount = millilitersDrunk.value,
            drinks = emptyList(),
            selectedDrink = selectedDrink.value,
            validatedMillilitersCount = validatedMillilitersCountState.value,
            updateState = updateState.value,
            creationAllowed = false
        )
    )

    fun updateWaterTrack() {
        viewModelScope.launch {
            require(validatedMillilitersCountState.value is CorrectMillilitersInput)
            waterTrackProvider.provideWaterTrackById(waterTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    date = selectedDate.value.getStartOfDay(),
                    millilitersCount = millilitersDrunk.value,
                    drinkType = selectedDrink.value.type
                )
                waterTrackUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw NotFoundException("Track not found by it's id")
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

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            waterTrackProvider.provideWaterTrackById(waterTrackId).firstOrNull()
                ?.let { waterTrack ->
                    millilitersDrunk.value = waterTrack.millilitersCount
                    selectedDrink.value = Drink(
                        type = waterTrack.drinkType,
                        image = drinkTypeImageResolver.resolve(waterTrack.drinkType)
                    )
                }
        }
    }

    sealed class UpdateState {
        object NotExecuted : UpdateState()
        object Executed : UpdateState()
    }

    data class State(
        val creationAllowed: Boolean,
        val updateState: UpdateState,
        val validatedMillilitersCount: ValidatedMillilitersCount?,
        val selectedDate: LocalDate,
        val millilitersCount: Int,
        val drinks: List<Drink>,
        val selectedDrink: Drink,
    )
}