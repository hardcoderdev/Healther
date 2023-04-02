package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingHistoryViewModel(
    private val moodTrackProvider: MoodTrackProvider,
    private val moodTrackDeleter: MoodTrackDeleter,
    private val moodWithHobbyProvider: MoodWithHobbyProvider,
    private val hobbyTrackProvider: HobbyTrackProvider
) : ViewModel() {

    private val selectedRange = MutableStateFlow(LocalDate.now().createRangeForCurrentDay())

    private val moodTrackList = selectedRange.flatMapLatest {   range ->
        moodTrackProvider.provideAllMoodTracksByDayRange(
            range.first..range.last
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }

    private val moodWithHobbyTrackList = moodTrackList.flatMapLatest { moodTrackList ->
        if (moodTrackList.isEmpty()) flowOf(emptyList())
        else combine(
            moodTrackList.map { moodTrack ->
                moodWithHobbyProvider.provideMoodWithHobbyTracks(moodTrack.id)
            }
        ) { moodWithHobbyTrackListArray ->
            moodTrackList.mapIndexed { index, moodTrack ->
                MoodTrackWithHobbies(
                    moodTrack = moodTrack,
                    hobbyTrackList = if (moodWithHobbyTrackListArray.isEmpty()) emptyList() else combine(
                        moodWithHobbyTrackListArray[index].map { moodWithHobbyTrack ->
                            hobbyTrackProvider.provideHobbyById(moodWithHobbyTrack.hobbyTrackId)
                        }) {
                        it.toList()
                    }.firstOrNull()
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(moodWithHobbyTrackList) { moodWithHobbyTrackList ->
        State(moodTrackWithHobbyList = moodWithHobbyTrackList[0])
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            moodTrackWithHobbyList = emptyList()
        )
    )

    fun updateSelectedRange(day: LocalDate) {
        selectedRange.value = day.getStartOfDay()..day.getEndOfDay()
    }

    fun deleteTrack(id: Int) {
        viewModelScope.launch {
            moodTrackDeleter.deleteById(id)
        }
    }

    data class State(val moodTrackWithHobbyList: List<MoodTrackWithHobbies>)
}