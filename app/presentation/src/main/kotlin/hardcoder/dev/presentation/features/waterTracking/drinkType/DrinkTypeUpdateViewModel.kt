package hardcoder.dev.presentation.features.waterTracking.drinkType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.validators.features.waterTracking.DrinkTypeNameValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DrinkTypeUpdateViewModel(
    drinkTypeId: Int,
    drinkTypeNameValidator: DrinkTypeNameValidator,
    drinkTypeProvider: DrinkTypeProvider,
    drinkTypeUpdater: DrinkTypeUpdater,
    drinkTypeDeleter: DrinkTypeDeleter,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

    private val initialDrinkType = MutableStateFlow<DrinkType?>(null)

    val nameInputController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = drinkTypeNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        items = iconResourceProvider.getIcons(),
    )

    val waterPercentageInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = 0,
    )

    val updateController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            drinkTypeUpdater.update(
                id = drinkTypeId,
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.getInput(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is hardcoder.dev.validators.features.waterTracking.CorrectDrinkTypeName
        },
    )

    val deletionController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            drinkTypeDeleter.deleteById(drinkTypeId)
        },
    )

    init {
        coroutineScope.launch {
            val drinkType = drinkTypeProvider.provideDrinkTypeById(drinkTypeId).first()!!
            initialDrinkType.value = drinkType
            nameInputController.changeInput(drinkType.name)
            waterPercentageInputController.changeInput(drinkType.hydrationIndexPercentage)
            iconSelectionController.select(drinkType.icon)
        }
    }
}