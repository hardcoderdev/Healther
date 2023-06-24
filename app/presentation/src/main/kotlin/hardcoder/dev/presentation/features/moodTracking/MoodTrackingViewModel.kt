package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatisticProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
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
    moodWithActivitiesProvider: MoodWithActivitiesProvider,
    moodTrackProvider: MoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
    moodTrackingStatisticProvider: MoodTrackingStatisticProvider
) : ViewModel() {

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

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodWithActivitiesProvider.provideMoodWithActivityList(
            LocalDate.createRangeForCurrentDay()
        )
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodTrackingStatisticProvider.provideMoodTrackingStatistic()
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodTracksForTheLastMonth.map { fastingTrackList ->
            fastingTrackList.groupBy {
                it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
            }.map { entry ->
                entry.key to entry.value.maxBy { it.moodType.positivePercentage }.moodType.positivePercentage
            }
        }
    )
}