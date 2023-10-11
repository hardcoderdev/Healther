package hardcoder.dev.presentation.features.waterTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logics.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logics.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.validators.features.waterTracking.WaterTrackMillilitersValidator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class WaterTrackingCreationViewModel(
    waterTrackCreator: WaterTrackCreator,
    waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    drinkTypeProvider: DrinkTypeProvider,
    dateTimeProvider: DateTimeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
) : ScreenModel {

    private val dailyWaterIntakeState = waterTrackingDailyRateProvider
        .provideDailyRateInMilliliters().stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = 0,
        )

    val drinkSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = drinkTypeProvider.provideAllDrinkTypes(),
    )

    val dateInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentTime().date,
    )

    val timeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val millilitersDrunkInputController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = 0,
        validation = {
            waterTrackMillilitersValidator.validate(
                millilitersCount = this,
                dailyWaterIntakeInMillisCount = dailyWaterIntakeState.value,
            )
        },
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
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
            millilitersCountState.validationResult is hardcoder.dev.validators.features.waterTracking.CorrectMillilitersCount &&
                drinkState is SingleSelectionController.State.Loaded
        },
    )
}