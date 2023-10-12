package hardcoder.dev.presentation.features.foodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logics.features.foodTracking.FoodTrackCreator
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider
import kotlinx.coroutines.flow.map

class FoodTrackingCreationViewModel(
    foodTrackCreator: FoodTrackCreator,
    foodTypeProvider: FoodTypeProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val foodSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = foodTypeProvider.provideAllFoodTypes(),
    )

    val caloriesInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = 0,
    )

    val dateInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentTime().date,
    )

    val timeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            foodTrackCreator.create(
                creationInstant = dateInputController.getInput().toInstant(timeInputController.getInput()),
                calories = caloriesInputController.getInput(),
                foodTypeId = foodSelectionController.requireSelectedItem().id,
            )
        },
        isAllowedFlow = foodSelectionController.state.map { foodSelectionState ->
            foodSelectionState is SingleSelectionController.State.Loaded
        },
    )
}