package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingHistoryViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = LocalDate.createRangeForCurrentDay()
    )

    val waterTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            waterTrackProvider.provideWaterTracksByDayRange(range.input)
        }.mapItems {
            it.toItem(
                resolvedMillilitersCount = waterPercentageResolver.resolve(
                    drinkType = it.drinkType,
                    millilitersDrunk = it.millilitersCount
                )
            )
        }
    )
}