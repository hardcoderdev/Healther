package hardcoder.dev.presentation.features.waterTracking.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.logic.features.waterTracking.drinkType.CorrectDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import kotlinx.coroutines.flow.map

class DrinkTypeCreationViewModel(
    drinkTypeCreator: DrinkTypeCreator,
    drinkTypeNameValidator: DrinkTypeNameValidator,
    iconResourceProvider: hardcoder.dev.icons.IconResourceProvider,
) : ViewModel() {

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = drinkTypeNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val waterPercentageInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_WATER_PERCENTAGE,
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeCreator.create(
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.getInput(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDrinkTypeName
        },
    )

    private companion object {
        const val DEFAULT_WATER_PERCENTAGE = 30
    }
}