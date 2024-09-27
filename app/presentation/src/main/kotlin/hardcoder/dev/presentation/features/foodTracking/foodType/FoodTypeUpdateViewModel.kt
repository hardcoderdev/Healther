package hardcoder.dev.presentation.features.foodTracking.foodType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.SwitchController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeDeleter
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeUpdater
import hardcoder.dev.validators.features.foodTracking.CorrectFoodTypeName
import hardcoder.dev.validators.features.foodTracking.FoodTypeNameValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FoodTypeUpdateViewModel(
    foodTypeId: Int,
    foodTypeNameValidator: FoodTypeNameValidator,
    foodTypeProvider: FoodTypeProvider,
    foodTypeUpdater: FoodTypeUpdater,
    foodTypeDeleter: FoodTypeDeleter,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

    private val initialFoodType = MutableStateFlow<FoodType?>(null)

    val nameInputController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = foodTypeNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        items = iconResourceProvider.getIcons(),
    )

    val spicinessToggleController = SwitchController(coroutineScope = coroutineScope)

    val vegetarianToggleController = SwitchController(coroutineScope = coroutineScope)

    val updateController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            foodTypeUpdater.update(
                id = foodTypeId,
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                isVegetarian = vegetarianToggleController.isActive(),
                isSpicy = spicinessToggleController.isActive(),
                proteins = 0.0f,
                fats = 0.0f,
                carbohydrates = 0.0f,
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectFoodTypeName
        },
    )

    val deletionController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            foodTypeDeleter.deleteById(foodTypeId)
        },
    )

    init {
        coroutineScope.launch {
            val foodType = foodTypeProvider.provideFoodTypeById(foodTypeId).first()!!
            initialFoodType.value = foodType
            nameInputController.changeInput(foodType.name)
            iconSelectionController.select(foodType.icon)
        }
    }
}