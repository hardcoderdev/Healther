package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logic.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.validators.CorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.validators.WaterTrackMillilitersValidator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class WaterTrackingCreationViewModel(
    waterTrackCreator: WaterTrackCreator,
    waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    drinkTypeProvider: DrinkTypeProvider,
    dateTimeProvider: DateTimeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
) : ViewModel() {

    private val dailyWaterIntakeState = waterTrackingDailyRateProvider
        .provideDailyRateInMilliliters().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0,
        )

    val drinkSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = drinkTypeProvider.provideAllDrinkTypes(),
    )

    val dateInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().date,
    )

    val timeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val millilitersDrunkInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = 0,
        validation = {
            waterTrackMillilitersValidator.validate(
                millilitersCount = this,
                dailyWaterIntakeInMillisCount = dailyWaterIntakeState.value,
            )
        },
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            waterTrackCreator.create(
                dateTime = dateInputController.getInput().toInstant(timeInputController.getInput()),
                millilitersCount = millilitersDrunkInputController.validateAndRequire(),
                drinkTypeId = drinkSelectionController.requireSelectedItem().id,
            )
        },
        isAllowedFlow = combine(
            drinkSelectionController.state,
            millilitersDrunkInputController.state,
        ) { drinkState, millilitersCountState ->
            millilitersCountState.validationResult is CorrectMillilitersCount &&
                drinkState is SingleSelectionController.State.Loaded
        },
    )
}