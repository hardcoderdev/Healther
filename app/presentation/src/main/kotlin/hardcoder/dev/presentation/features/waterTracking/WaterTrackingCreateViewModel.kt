package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.waterTracking.CorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logic.features.waterTracking.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class WaterTrackingCreateViewModel(
    waterTrackCreator: WaterTrackCreator,
    waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    drinkTypeProvider: DrinkTypeProvider,
    dateTimeProvider: DateTimeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider
) : ViewModel() {

    private val dailyWaterIntakeState = waterTrackingDailyRateProvider
        .provideDailyRateInMilliliters().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    val drinkSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = drinkTypeProvider.provideAllDrinkTypes()
    )

    val dateInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.getCurrentTime()
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

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            waterTrackCreator.createWaterTrack(
                date = dateInputController.state.value.input,
                millilitersCount = millilitersDrunkInputController.validateAndRequire(),
                drinkType = drinkSelectionController.requireSelectedItem()
            )
        },
        isAllowedFlow = combine(
            drinkSelectionController.state,
            millilitersDrunkInputController.state
        ) { drinkState, millilitersCountState ->
            millilitersCountState.validationResult is CorrectMillilitersCount
                    && drinkState is SingleSelectionController.State.Loaded
        }
    )
}