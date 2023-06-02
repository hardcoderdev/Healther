package hardcoder.dev.presentation.features.waterTracking.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.waterTracking.drinkType.CorrectDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map

class DrinkTypeCreateViewModel(
    drinkTypeCreator: DrinkTypeCreator,
    drinkTypeNameValidator: DrinkTypeNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = drinkTypeNameValidator::validate
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val waterPercentageInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_WATER_PERCENTAGE,
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeCreator.create(
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.state.value.input
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDrinkTypeName
        }
    )

    private companion object {
        const val DEFAULT_WATER_PERCENTAGE = 30
    }
}