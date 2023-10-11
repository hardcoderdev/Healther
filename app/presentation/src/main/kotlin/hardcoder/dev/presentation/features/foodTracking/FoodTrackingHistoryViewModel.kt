package hardcoder.dev.presentation.features.foodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
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
) : ScreenModel {

    val dateRangeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val foodTracksLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            foodTrackProvider.provideFoodTracksByDayRange(range.input)
        },
    )
}