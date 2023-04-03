package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.entities.features.moodTracking.MoodWithHobbies
import hardcoder.dev.logic.entities.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingViewModel(
    moodWithHobbyProvider: MoodWithHobbyProvider,
    moodTrackProvider: MoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
    moodTrackingStatisticProvider: MoodTrackingStatisticProvider
) : ViewModel() {

    private val moodWithHobbyTrackList = moodWithHobbyProvider.provideMoodWithHobbyList(
        LocalDate.now().createRangeForCurrentDay()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val moodTrackingStatistic =
        moodTrackingStatisticProvider.provideMoodTrackingStatistic().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val moodTracksForTheLastMonth =
        dateTimeProvider.currentTimeFlow().flatMapLatest { currentDateTime ->
            moodTrackProvider.provideAllMoodTracksByDayRange(
                currentDateTime.date.minus(
                    1, DateTimeUnit.MONTH
                ).getStartOfDay()..currentDateTime.date.getEndOfDay()
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
        }

    private val chartEntries = moodTracksForTheLastMonth.map { fastingTrackList ->
        fastingTrackList.groupBy {
            it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
        }.map { entry ->
            entry.key to entry.value.maxBy { it.moodType.positivePercentage }.moodType.positivePercentage
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        moodWithHobbyTrackList,
        moodTrackingStatistic,
        chartEntries,
    ) { moodWithHobbyTrackList, moodTrackingStatistic, chartEntries ->
        State(
            moodWithHobbiesList = moodWithHobbyTrackList,
            moodTrackingStatistic = moodTrackingStatistic,
            chartEntries = chartEntries
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            moodWithHobbiesList = moodWithHobbyTrackList.value,
            moodTrackingStatistic = moodTrackingStatistic.value,
            chartEntries = chartEntries.value
        )
    )

    data class State(
        val moodWithHobbiesList: List<MoodWithHobbies>,
        val moodTrackingStatistic: MoodTrackingStatistic?,
        val chartEntries: List<Pair<Int, Int>>
    )
}