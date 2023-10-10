package hardcoder.dev.presentation.features.moodTracking

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import hardcoder.dev.viewmodel.ViewModel

class MoodTrackingViewModel(
    moodWithActivitiesProvider: MoodWithActivitiesProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodWithActivitiesProvider.provideMoodWithActivityList(
            dayRange = dateTimeProvider.currentDateRange(),
        ),
    )
}