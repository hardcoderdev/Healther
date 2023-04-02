package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyCreator
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
    //private val diaryTrackCreator: DiaryTrackCreator,
    private val moodWithHobbyCreator: MoodWithHobbyCreator,
    moodTypeProvider: MoodTypeProvider,
    hobbyTrackProvider: HobbyTrackProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private var mutableSelectedHobbies = mutableListOf<HobbyTrack>()
    private val selectedDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val selectedMoodType = MutableStateFlow<MoodType?>(null)
    private val note = MutableStateFlow("")
    private val selectedHobbies = MutableStateFlow<List<HobbyTrack>>(mutableListOf())
    private val moodTypeList = moodTypeProvider.provideAllMoodTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    private val hobbyTrackList = hobbyTrackProvider.provideAllHobbies().stateIn(
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
        hobbyTrackList,
        selectedMoodType,
        selectedHobbies,
        selectedDate,
        note
    ) { creationState, moodTypeList, hobbyTrackList, selectedMoodType,
        selectedHobbies, selectedDate, note ->
        State(
            creationState = creationState,
            moodTypeList = moodTypeList,
            hobbyTrackList = hobbyTrackList,
            selectedMoodType = selectedMoodType,
            selectedHobbies = selectedHobbies,
            selectedDate = selectedDate,
            note = note
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationState = creationState.value,
            moodTypeList = moodTypeList.value,
            hobbyTrackList = hobbyTrackList.value,
            selectedMoodType = selectedMoodType.value,
            selectedHobbies = selectedHobbies.value,
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

    fun addHobbyTrack(hobbyTrack: HobbyTrack) {
        if (selectedHobbies.value.contains(hobbyTrack).not()) {
            mutableSelectedHobbies = selectedHobbies.value.toMutableList()
            mutableSelectedHobbies.add(hobbyTrack)
            selectedHobbies.value = mutableSelectedHobbies
        }
    }

    fun removeHobbyTrack(hobbyTrack: HobbyTrack) {
        mutableSelectedHobbies = selectedHobbies.value.toMutableList()
        mutableSelectedHobbies.remove(hobbyTrack)
        selectedHobbies.value = mutableSelectedHobbies
    }

    fun createTrack() {
        viewModelScope.launch {
            val selectedMoodType = requireNotNull(selectedMoodType.value)

            val moodTrackId = moodTrackCreator.create(
                moodType = selectedMoodType,
                date = selectedDate.value
            )

            if (note.value.isNotEmpty()) {
//                diaryTrackCreator.create(
//                    creationTime = creationTime,
//                    linkedFeatureType = FeatureType.MOOD_TRACKING,
//                    text = note.value,
//                    title = null
//                )
            }

            selectedHobbies.value.forEach { hobbyTrack ->
                moodWithHobbyCreator.create(
                    moodTrackId = moodTrackId,
                    hobbyId = hobbyTrack.id
                )
            }

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationState: CreationState,
        val moodTypeList: List<MoodType>,
        val hobbyTrackList: List<HobbyTrack>,
        val selectedMoodType: MoodType?,
        val selectedHobbies: List<HobbyTrack>,
        val selectedDate: LocalDateTime,
        val note: String?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}