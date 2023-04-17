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
    private var mutableSelectedActivities = mutableListOf<Activity>()
    private val selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val selectedMoodType = MutableStateFlow<MoodType?>(null)
    private val note = MutableStateFlow("")
    private val selectedActivities = MutableStateFlow<List<Activity>>(mutableListOf())
    private val moodTypeList = moodTypeProvider.provideAllMoodTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
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

    fun addActivity(activity: Activity) {
        if (selectedActivities.value.contains(activity).not()) {
            mutableSelectedActivities = selectedActivities.value.toMutableList()
            mutableSelectedActivities.add(activity)
            selectedActivities.value = mutableSelectedActivities
        }
    }

    fun removeActivity(activity: Activity) {
        mutableSelectedActivities = selectedActivities.value.toMutableList()
        mutableSelectedActivities.remove(activity)
        selectedActivities.value = mutableSelectedActivities
    }

    fun createTrack() {
        viewModelScope.launch {
            val selectedMoodType = requireNotNull(selectedMoodType.value)

            moodTrackCreator.create(
                note = note.value,
                moodType = selectedMoodType,
                date = selectedDate.value,
                selectedActivities = selectedActivities.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
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