package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MoodTrackingTrackUpdateViewModel(
    private val moodTrackId: Int,
    private val moodTrackUpdater: MoodTrackUpdater,
    private val moodTrackDeleter: MoodTrackDeleter,
    //private val diaryTrackCreator: DiaryTrackCreator,
    private val moodTrackProvider: MoodTrackProvider,
    moodWithActivityProvider: MoodWithActivitiesProvider,
    activityProvider: ActivityProvider,
    moodTypeProvider: MoodTypeProvider,
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private var mutableSelectedActivities = mutableListOf<Activity>()
    private val selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val selectedMoodType = MutableStateFlow<MoodType?>(null)
    private val note = MutableStateFlow("")
    private val selectedActivities = MutableStateFlow<List<Activity>>(emptyList())
    private val initialActivities = moodWithActivityProvider.provideActivityListByMoodTrackId(moodTrackId).map {
        selectedActivities.value = it
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

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
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        moodTypeList,
        activityList,
        selectedMoodType,
        selectedActivities,
        selectedDate,
        note
    ) { updateState, deleteState, moodTypeList, hobbyTrackList,
        selectedMoodType, selectedHobbies, selectedDate, note ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            moodTypeList = moodTypeList,
            activityList = hobbyTrackList,
            selectedMoodType = selectedMoodType,
            selectedHobbies = selectedHobbies,
            selectedDate = selectedDate,
            note = note
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            moodTypeList = moodTypeList.value,
            activityList = activityList.value,
            selectedMoodType = selectedMoodType.value,
            selectedHobbies = initialActivities.value,
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

    fun updateTrack() {
        viewModelScope.launch {
            moodTrackProvider.provideById(moodTrackId).firstOrNull()?.let {
                val selectedMoodType = requireNotNull(selectedMoodType.value)
                val moodTrack = it.copy(
                    moodType = selectedMoodType,
                    date = selectedDate.value.toInstant(TimeZone.currentSystemDefault())
                )

                moodTrackUpdater.update(moodTrack, selectedActivities.value)
            }

            updateState.value = UpdateState.Executed
        }
    }

    fun deleteTrack() {
        viewModelScope.launch {
            moodTrackDeleter.deleteById(moodTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            selectedMoodType.value = moodTypeList.value.firstOrNull()

            moodTrackProvider.provideById(moodTrackId).firstOrNull()?.let { moodTrack ->
                selectedMoodType.value = moodTrack.moodType
                selectedDate.value = moodTrack.date.toLocalDateTime(TimeZone.currentSystemDefault())
                selectedActivities.value = initialActivities.value
            }
        }
    }

    sealed class UpdateState {
        object Executed : UpdateState()
        object NotExecuted : UpdateState()
    }

    sealed class DeleteState {
        object Executed : DeleteState()
        object NotExecuted : DeleteState()
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val moodTypeList: List<MoodType>,
        val activityList: List<Activity>,
        val selectedMoodType: MoodType?,
        val selectedHobbies: List<Activity>,
        val selectedDate: LocalDateTime,
        val note: String?
    )
}