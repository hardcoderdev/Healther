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
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.CorrectDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeUpdater
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
) : ViewModel() {

    private val initialDrinkType = MutableStateFlow<DrinkType?>(null)

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
        initialInput = 0,
    )

    val updateController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeUpdater.update(
                id = drinkTypeId,
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.getInput(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDrinkTypeName
        },
    )

    val deletionController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeDeleter.deleteById(drinkTypeId)
        },
    )

    init {
        viewModelScope.launch {
            val drinkType = drinkTypeProvider.provideDrinkTypeById(drinkTypeId).first()!!
            initialDrinkType.value = drinkType
            nameInputController.changeInput(drinkType.name)
            waterPercentageInputController.changeInput(drinkType.hydrationIndexPercentage)
            iconSelectionController.select(drinkType.icon)
        }
    }
}