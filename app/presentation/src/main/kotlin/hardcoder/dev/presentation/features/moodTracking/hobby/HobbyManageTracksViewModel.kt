package hardcoder.dev.presentation.features.moodTracking.hobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HobbyManageTracksViewModel(hobbyTrackProvider: HobbyTrackProvider) : ViewModel() {

    val state = hobbyTrackProvider.provideAllHobbies().map { hobbyTrackList ->
        State(hobbyTrackList = hobbyTrackList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            hobbyTrackList = emptyList()
        )
    )

    data class State(val hobbyTrackList: List<HobbyTrack>)
}