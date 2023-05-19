package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MoodTrackingTrackCreateViewModel(
    private val moodTrackCreator: MoodTrackCreator,
    moodTypeProvider: MoodTypeProvider,
    activityProvider: ActivityProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val selectedMoodType = MutableStateFlow<MoodType?>(null)
    private val note = MutableStateFlow<String?>(null)
    private val moodTypeList = moodTypeProvider.provideAllMoodTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    private val selectedActivities = MutableStateFlow<List<Activity>>(mutableListOf())
    private val activityList = activityProvider.provideAllActivities().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            selectedMoodType.value = moodTypeList.value.firstOrNull()
        }
    }

    val state = combine(
        creationState,
        moodTypeList,
        activityList,
        selectedMoodType,
        selectedActivities,
        selectedDate,
        note
    ) { creationState, moodTypeList, activityList, selectedMoodType,
        selectedActivities, selectedDate, note ->
        State(
            creationAllowed = selectedMoodType != null,
            creationState = creationState,
            moodTypeList = moodTypeList,
            activityList = activityList,
            selectedMoodType = selectedMoodType,
            selectedActivities = selectedActivities,
            selectedDate = selectedDate,
            note = note
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            moodTypeList = moodTypeList.value,
            activityList = activityList.value,
            selectedMoodType = selectedMoodType.value,
            selectedActivities = selectedActivities.value,
            selectedDate = selectedDate.value,
            note = note.value
        )
    )

    fun updateSelectedMoodType(type: MoodType) {
        selectedMoodType.value = type
    }

    fun updateNote(attachedNote: String) {
        note.value = attachedNote
    }

    fun updateSelectedDate(localDateTime: LocalDateTime) {
        selectedDate.value = localDateTime
    }

    fun toggleActivity(activity: Activity) {
        val selectedActivitiesMutableList = selectedActivities.value.toMutableList()
        val isRemoved = selectedActivitiesMutableList.removeIf { it == activity }
        if (isRemoved) {
            selectedActivities.value = selectedActivitiesMutableList
            return
        } else {
            selectedActivitiesMutableList.add(activity)
            selectedActivities.value = selectedActivitiesMutableList
        }
    }

    fun createTrack() {
        viewModelScope.launch {
            moodTrackCreator.create(
                note = note.value,
                moodType = requireNotNull(selectedMoodType.value),
                date = selectedDate.value,
                selectedActivities = selectedActivities.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationAllowed: Boolean,
        val creationState: CreationState,
        val moodTypeList: List<MoodType>,
        val activityList: List<Activity>,
        val selectedMoodType: MoodType?,
        val selectedActivities: List<Activity>,
        val selectedDate: LocalDateTime,
        val note: String?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}