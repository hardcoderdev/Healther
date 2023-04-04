package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManageActivitiesViewModel(activityProvider: ActivityProvider) : ViewModel() {

    val state = activityProvider.provideAllActivities().map { hobbyTrackList ->
        State(activityList = hobbyTrackList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            activityList = emptyList()
        )
    )

    data class State(val activityList: List<Activity>)
}