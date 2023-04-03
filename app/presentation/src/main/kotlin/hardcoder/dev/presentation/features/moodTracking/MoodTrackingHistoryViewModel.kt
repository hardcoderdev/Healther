package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.entities.features.moodTracking.MoodWithHobbies
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingHistoryViewModel(
    private val moodTrackDeleter: MoodTrackDeleter,
    private val moodWithHobbyProvider: MoodWithHobbyProvider
) : ViewModel() {

    private val selectedRange = MutableStateFlow(LocalDate.now().createRangeForCurrentDay())

    private val moodWithHobbyTrackList = selectedRange.flatMapLatest { range ->
        moodWithHobbyProvider.provideMoodWithHobbyList(range)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = moodWithHobbyTrackList.map { moodWithHobbyTrackList ->
        State(moodTrackWithHobbyList = moodWithHobbyTrackList)
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

    data class State(val moodTrackWithHobbyList: List<MoodWithHobbies>)
}