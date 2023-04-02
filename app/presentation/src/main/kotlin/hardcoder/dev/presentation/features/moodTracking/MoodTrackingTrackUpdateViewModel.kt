package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyCreator
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyDeleter
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    private val moodWithHobbyCreator: MoodWithHobbyCreator,
    private val moodWithHobbyDeleter: MoodWithHobbyDeleter,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodWithHobbyProvider: MoodWithHobbyProvider,
    private val hobbyTrackProvider: HobbyTrackProvider,
    moodTypeProvider: MoodTypeProvider,
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private var mutableSelectedHobbies = mutableListOf<HobbyTrack>()
    private val selectedDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val selectedMoodType = MutableStateFlow<MoodType?>(null)
    private val note = MutableStateFlow("")
    private val initialHobbies = MutableStateFlow<List<HobbyTrack>>(emptyList())
    private val selectedHobbies = MutableStateFlow<List<HobbyTrack>>(emptyList())
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
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        moodTypeList,
        hobbyTrackList,
        selectedMoodType,
        selectedHobbies,
        selectedDate,
        note
    ) { updateState, deleteState, moodTypeList, hobbyTrackList,
        selectedMoodType, selectedHobbies, selectedDate, note ->
        State(
            updateState = updateState,
            deleteState = deleteState,
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
            updateState = updateState.value,
            deleteState = deleteState.value,
            moodTypeList = moodTypeList.value,
            hobbyTrackList = hobbyTrackList.value,
            selectedMoodType = selectedMoodType.value,
            selectedHobbies = initialHobbies.value,
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

    fun updateTrack() {
        viewModelScope.launch {
            moodTrackProvider.provideById(moodTrackId).firstOrNull()?.let {
                val selectedMoodType = requireNotNull(selectedMoodType.value)

                val moodTrack = it.copy(
                    moodType = selectedMoodType, date = selectedDate.value.toInstant(
                        TimeZone.currentSystemDefault()
                    )
                )

                moodTrackUpdater.update(moodTrack)

                if (initialHobbies.value.isNotEmpty()) {
                    moodWithHobbyDeleter.deleteAllHobbiesByMoodTrackId(moodTrackId)
                }

                selectedHobbies.value.forEach { hobbyTrack ->
                    moodWithHobbyCreator.create(
                        moodTrackId = moodTrackId,
                        hobbyId = hobbyTrack.id
                    )
                }
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

                initialHobbies.value = moodWithHobbyProvider.provideMoodWithHobbyTracks(
                    moodTrackId
                ).map { moodWithHobbyTrackList ->
                    if (moodWithHobbyTrackList.isEmpty()) emptyList() else combine(
                        moodWithHobbyTrackList.map { moodWithHobbyTrack ->
                            hobbyTrackProvider.provideHobbyById(moodWithHobbyTrack.hobbyTrackId)
                        }) {
                        if (it.isEmpty()) emptyList() else it.filterNotNull().toList()
                    }.first()
                }.first()

                selectedHobbies.value = initialHobbies.value
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
        val hobbyTrackList: List<HobbyTrack>,
        val selectedMoodType: MoodType?,
        val selectedHobbies: List<HobbyTrack>,
        val selectedDate: LocalDateTime,
        val note: String?
    )
}