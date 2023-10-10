package hardcoder.dev.presentation.features.moodTracking.activity

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.viewmodel.ViewModel

class MoodActivitiesViewModel(moodActivityProvider: MoodActivityProvider) : ViewModel() {

    val activitiesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodActivityProvider.provideAllActivities(),
    )
}