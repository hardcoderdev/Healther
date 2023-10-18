package hardcoder.dev.presentation.features.foodTracking.foodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeCreator
import hardcoder.dev.validators.features.foodTracking.CorrectFoodTypeName
import hardcoder.dev.validators.features.foodTracking.FoodTypeNameValidator
import kotlinx.coroutines.flow.map

class FoodTypeCreationViewModel(
    foodTypeCreator: FoodTypeCreator,
    foodTypeNameValidator: FoodTypeNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = foodTypeNameValidator::validate,
    )

    val iconSingleSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            foodTypeCreator.create(
                name = nameInputController.validateAndRequire(),
                icon = iconSingleSelectionController.requireSelectedItem(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectFoodTypeName
        },
    )
}