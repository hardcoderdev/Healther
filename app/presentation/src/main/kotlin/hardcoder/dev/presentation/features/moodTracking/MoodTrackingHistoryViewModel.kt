package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingHistoryViewModel(
    private val moodWithActivitiesProvider: MoodWithActivitiesProvider
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = LocalDate.createRangeForCurrentDay()
    )

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            moodWithActivitiesProvider.provideMoodWithActivityList(range.input)
        }
    )
}