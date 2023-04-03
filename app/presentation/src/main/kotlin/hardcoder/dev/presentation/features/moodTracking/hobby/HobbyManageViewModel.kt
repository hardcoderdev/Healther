package hardcoder.dev.presentation.features.moodTracking.hobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.entities.features.moodTracking.Hobby
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HobbyManageViewModel(hobbyProvider: HobbyProvider) : ViewModel() {

    val state = hobbyProvider.provideAllHobbies().map { hobbyTrackList ->
        State(hobbyList = hobbyTrackList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            hobbyList = emptyList()
        )
    )

    data class State(val hobbyList: List<Hobby>)
}