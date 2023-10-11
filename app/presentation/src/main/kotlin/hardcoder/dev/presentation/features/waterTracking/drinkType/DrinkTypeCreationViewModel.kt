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
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.validators.features.waterTracking.DrinkTypeNameValidator
import kotlinx.coroutines.flow.map

class DrinkTypeCreationViewModel(
    drinkTypeCreator: DrinkTypeCreator,
    drinkTypeNameValidator: DrinkTypeNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

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
        initialInput = DEFAULT_WATER_PERCENTAGE,
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            drinkTypeCreator.create(
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.getInput(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is hardcoder.dev.validators.features.waterTracking.CorrectDrinkTypeName
        },
    )

    private companion object {
        const val DEFAULT_WATER_PERCENTAGE = 30
    }
}