package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.waterTracking.CorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.WaterTrack
import hardcoder.dev.logic.features.waterTracking.WaterTrackDeleter
import hardcoder.dev.logic.features.waterTracking.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackUpdater
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class WaterTrackingUpdateViewModel(
    private val waterTrackId: Int,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val waterTrackUpdater: WaterTrackUpdater,
    private val waterTrackProvider: WaterTrackProvider,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    drinkTypeProvider: DrinkTypeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider
) : ViewModel() {

    private val initialWaterTrack = MutableStateFlow<WaterTrack?>(null)

    private val dailyWaterIntakeState = waterTrackingDailyRateProvider
        .provideDailyRateInMilliliters().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    val dateInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    val millilitersDrunkInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = 0,
        validation = {
            waterTrackMillilitersValidator.validate(
                millilitersCount = this,
                dailyWaterIntakeInMillisCount = dailyWaterIntakeState.value
            )
        }
    )

    val drinkSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = drinkTypeProvider.provideAllDrinkTypes()
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            waterTrackUpdater.update(
                id = waterTrackId,
                date = dateInputController.state.value.input,
                millilitersCount = millilitersDrunkInputController.validateAndRequire(),
                drinkTypeId = drinkSelectionController.requireSelectedItem().id
            )
        },
        isAllowedFlow = kotlinx.coroutines.flow.combine(
            drinkSelectionController.state,
            millilitersDrunkInputController.state
        ) { drinkState, millilitersCountState ->
            millilitersCountState.validationResult is CorrectMillilitersCount
                    && drinkState is SingleSelectionController.State.Loaded
        }
    )

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            waterTrackDeleter.deleteById(waterTrackId)
        }
    )

    init {
        viewModelScope.launch {
            val waterTrack = waterTrackProvider.provideWaterTrackById(waterTrackId).first()!!
            initialWaterTrack.value = waterTrack
            dateInputController.changeInput(waterTrack.date.toLocalDateTime(TimeZone.currentSystemDefault()))
            millilitersDrunkInputController.changeInput(waterTrack.millilitersCount)
            drinkSelectionController.select(waterTrack.drinkType)
        }
    }
}