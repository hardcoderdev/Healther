package hardcoder.dev.presentation.features.moodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingHistoryViewModel(
    private val moodWithActivitiesProvider: MoodWithActivitiesProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val dateRangeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            moodWithActivitiesProvider.provideMoodWithActivityList(range.input)
        },
    )
}