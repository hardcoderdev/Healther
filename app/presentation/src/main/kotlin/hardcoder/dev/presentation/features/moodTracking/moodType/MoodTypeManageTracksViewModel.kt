package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MoodTypeManageTracksViewModel(moodTypeProvider: MoodTypeProvider) : ViewModel() {

    val state = moodTypeProvider.provideAllMoodTypes().map { moodTypeList ->
        State(moodTypeList = moodTypeList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            moodTypeList = emptyList()
        )
    )

    data class State(val moodTypeList: List<MoodType>)
}