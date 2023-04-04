package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
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
    private val moodWithActivitiesProvider: MoodWithActivitiesProvider
) : ViewModel() {

    private val selectedRange = MutableStateFlow(LocalDate.now().createRangeForCurrentDay())

    private val moodWithActivityList = selectedRange.flatMapLatest { range ->
        moodWithActivitiesProvider.provideMoodWithActivityList(range)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = moodWithActivityList.map { moodWithActivityList ->
        State(moodWithActivityList = moodWithActivityList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            moodWithActivityList = emptyList()
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

    data class State(val moodWithActivityList: List<MoodWithActivities>)
}