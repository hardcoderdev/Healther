package hardcoder.dev.presentation.features.moodTracking.moodType

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.viewmodel.ViewModel

class MoodTypesViewModel(moodTypeProvider: MoodTypeProvider) : ViewModel() {

    val moodTypesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodTypeProvider.provideAllMoodTypes(),
    )
}