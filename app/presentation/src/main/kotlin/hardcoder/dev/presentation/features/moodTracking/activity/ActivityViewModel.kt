package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider

class ActivityViewModel(moodActivityProvider: MoodActivityProvider) : ViewModel() {

    val activitiesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodActivityProvider.provideAllActivities()
    )
}