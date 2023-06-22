package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider

class MoodTypeViewModel(moodTypeProvider: MoodTypeProvider) : ViewModel() {

    val moodTypesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodTypeProvider.provideAllMoodTypes()
    )
}