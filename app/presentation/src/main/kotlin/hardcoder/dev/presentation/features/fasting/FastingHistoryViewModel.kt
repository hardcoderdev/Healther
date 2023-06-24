package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class FastingHistoryViewModel(
    private val fastingTrackProvider: FastingTrackProvider
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = LocalDate.createRangeForCurrentDay()
    )

    val fastingTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            fastingTrackProvider.provideFastingTracksByStartTime(range.input)
        }
    )
}