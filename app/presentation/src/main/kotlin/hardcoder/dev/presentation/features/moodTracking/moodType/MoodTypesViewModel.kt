package hardcoder.dev.presentation.features.moodTracking.moodType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider

class MoodTypesViewModel(moodTypeProvider: MoodTypeProvider) : ScreenModel {

    val moodTypesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = moodTypeProvider.provideAllMoodTypes(),
    )
}