package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider

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