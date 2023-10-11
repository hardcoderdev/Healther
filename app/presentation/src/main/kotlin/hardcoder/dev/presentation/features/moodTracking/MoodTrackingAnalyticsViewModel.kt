package hardcoder.dev.presentation.features.moodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.entities.features.moodTracking.MoodTrackingChartData
import hardcoder.dev.entities.features.moodTracking.MoodTrackingChartEntry
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logics.features.moodTracking.statistic.MoodTrackingStatisticProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

class MoodTrackingAnalyticsViewModel(
    moodTrackProvider: MoodTrackProvider,
    moodTrackingStatisticProvider: MoodTrackingStatisticProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val moodTracksForTheLastMonth =
        dateTimeProvider.currentDateFlow().map { currentDate ->
            dateTimeProvider.dateRange(
                startDate = currentDate.minus(1, DateTimeUnit.MONTH),
                endDate = currentDate,
            )
        }.distinctUntilChanged().flatMapLatest { range ->
            moodTrackProvider.provideAllMoodTracksByDayRange(range)
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    val statisticLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = moodTrackingStatisticProvider.provideMoodTrackingStatistic(),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = moodTracksForTheLastMonth.map { fastingTrackList ->
            MoodTrackingChartData(
                entriesList = fastingTrackList.groupBy {
                    it.date.toLocalDateTime().dayOfMonth
                }.map { entry ->
                    MoodTrackingChartEntry(
                        from = entry.key,
                        to = entry.value.maxBy {
                            it.moodType.positivePercentage
                        }.moodType.positivePercentage,
                    )
                },
            )
        }.distinctUntilChanged(),
    )
}