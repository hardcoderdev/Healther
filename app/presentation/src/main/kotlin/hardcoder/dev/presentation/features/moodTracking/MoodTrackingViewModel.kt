package hardcoder.dev.presentation.features.moodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider

class MoodTrackingViewModel(
    moodWithActivitiesProvider: MoodWithActivitiesProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = moodWithActivitiesProvider.provideMoodWithActivityList(
            dayRange = dateTimeProvider.currentDateRange(),
        ),
    )
}