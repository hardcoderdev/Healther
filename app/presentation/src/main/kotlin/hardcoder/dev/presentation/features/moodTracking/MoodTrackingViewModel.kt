package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingViewModel(
    private val hobbyTrackProvider: HobbyTrackProvider,
    private val moodWithHobbyProvider: MoodWithHobbyProvider,
    moodTrackProvider: MoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
    moodTrackingStatisticProvider: MoodTrackingStatisticProvider
) : ViewModel() {

    private val moodTrackList = moodTrackProvider.provideAllMoodTracksByDayRange(
        LocalDate.now().createRangeForCurrentDay()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

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
            moodTrackWithHobbiesList = moodWithHobbyTrackList,
            moodTrackingStatistic = moodTrackingStatistic,
            chartEntries = chartEntries
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            moodTrackWithHobbiesList = moodWithHobbyTrackList.value,
            moodTrackingStatistic = moodTrackingStatistic.value,
            chartEntries = chartEntries.value
        )
    )

    data class State(
        val moodTrackWithHobbiesList: List<MoodTrackWithHobbies>,
        val moodTrackingStatistic: List<MoodTrackingStatistic>?,
        val chartEntries: List<Pair<Int, Int>>
    )
}