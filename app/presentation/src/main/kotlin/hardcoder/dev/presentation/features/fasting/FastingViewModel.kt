package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.math.safeDiv
import kotlin.time.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

@OptIn(ExperimentalCoroutinesApi::class)
class FastingViewModel(
    private val currentFastingManager: CurrentFastingManager,
    fastingTrackProvider: FastingTrackProvider,
    statisticProvider: FastingStatisticProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    private val fastingTracksForTheLastMonth =
        dateTimeProvider.currentDateFlow().flatMapLatest { currentDate ->
            fastingTrackProvider.provideFastingTracksByStartTime(
                dayRange = dateTimeProvider.dateRange(
                    startDate = currentDate.minus(1, DateTimeUnit.MONTH),
                    endDate = currentDate,
                ),
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )
        }

    val noteInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = "",
    )

    val lastThreeFastingTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = fastingTrackProvider.provideLastFastingTracks(
            limitCount = 3,
        ),
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = statisticProvider.provideFastingStatistic(),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = fastingTracksForTheLastMonth.map { fastingTrackList ->
            FastingChartData(
                entriesList = fastingTrackList.groupBy {
                    it.startTime.toLocalDateTime().dayOfMonth
                }.map { entry ->
                    entry.key to entry.value.maxBy { it.duration }
                }.map { (fastingDayOfMonth, fastingTrack) ->
                    val fastingDuration = fastingTrack.interruptedTime?.let {
                        it - fastingTrack.startTime
                    } ?: fastingTrack.duration

                    FastingChartEntry(
                        from = fastingDayOfMonth,
                        to = fastingDuration.inWholeHours,
                    )
                },
            )
        },
    )

    val fastingStateLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            dateTimeProvider.currentTimeFlow(),
            currentFastingManager.provideCurrentFastingTrack(),
        ) { currentTime, currentTrack ->
            currentTrack?.let {
                val timeSinceStartOfFasting = currentTime.toInstant(TimeZone.currentSystemDefault()) - currentTrack.startTime
                val isFinished = timeSinceStartOfFasting >= currentTrack.duration || currentTrack.interruptedTime != null

                if (isFinished) {
                    FastingState.Finished(
                        fastingPlan = currentTrack.fastingPlan,
                        isInterrupted = currentTrack.interruptedTime != null,
                        timeLeftInMillis = currentTrack.interruptedTime?.let {
                            it - currentTrack.startTime
                        } ?: run {
                            currentTime.toInstant(TimeZone.currentSystemDefault()) - currentTrack.startTime
                        },
                    )
                } else {
                    FastingState.Fasting(
                        selectedPlan = currentTrack.fastingPlan,
                        durationInMillis = currentTrack.duration,
                        startTimeInMillis = currentTrack.startTime,
                        timeLeftInMillis = timeSinceStartOfFasting,
                        fastingProgress = timeSinceStartOfFasting.inWholeMilliseconds safeDiv currentTrack.duration.inWholeMilliseconds,
                    )
                }
            } ?: run {
                FastingState.NotFasting
            }
        },
    )

    val interruptFastingController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currentFastingManager.interruptFasting()
        },
    )

    sealed class FastingState {
        data object NotFasting : FastingState()

        data class Fasting(
            val selectedPlan: FastingPlan,
            val startTimeInMillis: Instant,
            val durationInMillis: Duration,
            val timeLeftInMillis: Duration,
            val fastingProgress: Float,
        ) : FastingState()

        data class Finished(
            val timeLeftInMillis: Duration,
            val isInterrupted: Boolean,
            val fastingPlan: FastingPlan,
        ) : FastingState()
    }
}