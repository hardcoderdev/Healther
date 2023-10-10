package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingHistoryViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val waterTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            waterTrackProvider.provideWaterTracksByDayRange(range.input)
        }.mapItems {
            it.toItem(
                resolvedMillilitersCount = waterPercentageResolver.resolve(
                    drinkType = it.drinkType,
                    millilitersDrunk = it.millilitersCount,
                ),
            )
        },
    )
}