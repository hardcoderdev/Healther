package hardcoder.dev.presentation.features.waterBalance

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.waterBalance.CorrectMillilitersInput
import hardcoder.dev.logic.features.waterBalance.MillilitersCount
import hardcoder.dev.logic.features.waterBalance.ValidatedMillilitersCount
import hardcoder.dev.logic.features.waterBalance.WaterIntakeResolver
import hardcoder.dev.logic.features.waterBalance.WaterTrackDeleter
import hardcoder.dev.logic.features.waterBalance.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterBalance.WaterTrackProvider
import hardcoder.dev.logic.features.waterBalance.WaterTrackUpdater
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.hero.HeroProvider
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
    drinkTypeProvider: DrinkTypeProvider,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val waterTrackUpdater: WaterTrackUpdater,
    private val waterTrackProvider: WaterTrackProvider,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    private val waterIntakeResolver: WaterIntakeResolver
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val millilitersDrunk = MutableStateFlow(0)
    private val selectedDrink = MutableStateFlow<DrinkType?>(null)
    private val drinkTypes = drinkTypeProvider.provideAllDrinkTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    private val hero = heroProvider.requireHero()
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
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
        deleteState,
        selectedDate,
        millilitersDrunk,
        drinkTypes,
        selectedDrink,
        validatedMillilitersCountState
    ) { updateState, deleteState, selectedDate, millilitersDrunk, drinkTypes, selectedDrink, validatedMillilitersCountState ->
        State(
            updateState = updateState,
            deleteState = deleteState,
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
            deleteState = deleteState.value,
            creationAllowed = false
        )
    )

    fun updateWaterDrunk(waterDrunkInMilliliters: Int) {
        millilitersDrunk.value = waterDrunkInMilliliters
    }

    fun updateSelectedDrink(drink: DrinkType) {
        selectedDrink.value = drink
    }

    fun updateSelectedDate(localDate: LocalDate) {
        selectedDate.value = localDate
    }

    fun updateWaterTrack() {
        viewModelScope.launch {
            require(validatedMillilitersCountState.value is CorrectMillilitersInput)
            val selectedDrink = requireNotNull(selectedDrink.value)

            waterTrackProvider.provideWaterTrackById(waterTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    date = selectedDate.value.getStartOfDay(),
                    millilitersCount = millilitersDrunk.value,
                    drinkType = selectedDrink
                )
                waterTrackUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw NotFoundException("Track not found by it's id")
        }
    }

    fun deleteWaterTrack() {
        viewModelScope.launch {
            waterTrackDeleter.deleteById(waterTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            selectedDrink.value = drinkTypes.value.first()

            waterTrackProvider.provideWaterTrackById(waterTrackId).firstOrNull()
                ?.let { waterTrack ->
                    millilitersDrunk.value = waterTrack.millilitersCount
                    selectedDrink.value = waterTrack.drinkType
                }
        }
    }

    sealed class UpdateState {
        object NotExecuted : UpdateState()
        object Executed : UpdateState()
    }

    sealed class DeleteState {
        object NotExecuted : DeleteState()
        object Executed : DeleteState()
    }

    data class State(
        val creationAllowed: Boolean,
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val validatedMillilitersCount: ValidatedMillilitersCount?,
        val selectedDate: LocalDate,
        val millilitersCount: Int,
        val drinks: List<DrinkType>,
        val selectedDrink: DrinkType?,
    )
}