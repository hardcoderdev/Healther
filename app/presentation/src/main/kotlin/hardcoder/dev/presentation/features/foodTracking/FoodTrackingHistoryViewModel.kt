package hardcoder.dev.presentation.features.foodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class FoodTrackingHistoryViewModel(
    private val foodTrackProvider: FoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val foodTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            foodTrackProvider.provideFoodTracksByDayRange(range.input)
        },
    )
}