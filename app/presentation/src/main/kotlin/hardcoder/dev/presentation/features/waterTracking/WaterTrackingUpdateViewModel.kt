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
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.entities.features.waterTracking.WaterTrack
import hardcoder.dev.logics.features.waterTracking.WaterTrackDeleter
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackUpdater
import hardcoder.dev.logics.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.validators.features.waterTracking.WaterTrackMillilitersValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterTrackingUpdateViewModel(
    private val waterTrackId: Int,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val waterTrackUpdater: WaterTrackUpdater,
    private val waterTrackProvider: WaterTrackProvider,
    private val waterTrackMillilitersValidator: WaterTrackMillilitersValidator,
    dateTimeProvider: DateTimeProvider,
    drinkTypeProvider: DrinkTypeProvider,
    waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
) : ScreenModel {

    private val initialWaterTrack = MutableStateFlow<WaterTrack?>(null)

    private val dailyWaterIntakeState = waterTrackingDailyRateProvider
        .provideDailyRateInMilliliters().stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = 0,
        )

    val dateInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentDate(),
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

    val drinkSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = drinkTypeProvider.provideAllDrinkTypes(),
    )

    val updatingController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            waterTrackUpdater.update(
                id = waterTrackId,
                date = dateInputController.getInput().toInstant(timeInputController.getInput()),
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

    val deletionController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            waterTrackDeleter.deleteById(waterTrackId)
        },
    )

    init {
        coroutineScope.launch {
            val waterTrack = waterTrackProvider.provideWaterTrackById(waterTrackId).first()!!
            val waterTrackLocalDateTime = waterTrack.date.toLocalDateTime()

            initialWaterTrack.value = waterTrack
            dateInputController.changeInput(waterTrackLocalDateTime.date)
            timeInputController.changeInput(waterTrackLocalDateTime.time)
            millilitersDrunkInputController.changeInput(waterTrack.millilitersCount)
            drinkSelectionController.select(waterTrack.drinkType)
        }
    }
}