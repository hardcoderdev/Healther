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
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.icons.IconResourceProvider
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
        validation = drinkTypeNameValidator::validate
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val waterPercentageInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = 0,
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeUpdater.update(
                id = drinkTypeId,
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                hydrationIndexPercentage = waterPercentageInputController.state.value.input
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDrinkTypeName
        }
    )

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            drinkTypeDeleter.deleteById(drinkTypeId)
        }
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