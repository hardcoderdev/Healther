package hardcoder.dev.presentation.features.foodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.entities.features.foodTracking.FoodTrack
import hardcoder.dev.logics.features.foodTracking.FoodTrackDeleter
import hardcoder.dev.logics.features.foodTracking.FoodTrackProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackUpdater
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FoodTrackingUpdateViewModel(
    foodTrackId: Int,
    foodTrackUpdater: FoodTrackUpdater,
    foodTrackDeleter: FoodTrackDeleter,
    foodTypeProvider: FoodTypeProvider,
    foodTrackProvider: FoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    private val initialFoodTrack = MutableStateFlow<FoodTrack?>(null)

    val foodSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = foodTypeProvider.provideAllFoodTypes(),
    )

    val caloriesInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = 0,
    )

    val dateInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().date,
    )

    val timeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val updateController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            foodTrackUpdater.update(
                id = foodTrackId,
                creationInstant = dateInputController.getInput().toInstant(timeInputController.getInput()),
                calories = caloriesInputController.getInput(),
                foodTypeId = foodSelectionController.requireSelectedItem().id,
            )
        },
        isAllowedFlow = foodSelectionController.state.map { foodSelectionState ->
            foodSelectionState is SingleSelectionController.State.Loaded
        },
    )

    val deletionController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            foodTrackDeleter.deleteById(foodTrackId)
        },
    )

    init {
        viewModelScope.launch {
            val foodTrack = foodTrackProvider.provideFoodTrackById(foodTrackId).first()!!
            val foodTrackDateTime = foodTrack.creationInstant.toLocalDateTime()
            initialFoodTrack.value = foodTrack
            foodSelectionController.select(foodTrack.foodType)
            caloriesInputController.changeInput(foodTrack.calories)
            dateInputController.changeInput(foodTrackDateTime.date)
            timeInputController.changeInput(foodTrackDateTime.time)
        }
    }
}