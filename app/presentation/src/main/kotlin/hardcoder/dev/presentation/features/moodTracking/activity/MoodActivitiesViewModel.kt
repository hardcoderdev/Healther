package hardcoder.dev.presentation.features.moodTracking.activity

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider

class MoodActivitiesViewModel(moodActivityProvider: MoodActivityProvider) : ScreenModel {

    val activitiesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = moodActivityProvider.provideAllActivities(),
    )
}