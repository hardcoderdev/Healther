package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
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
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class FastingViewModel(
    private val currentFastingManager: CurrentFastingManager,
    fastingTrackProvider: FastingTrackProvider,
    statisticProvider: FastingStatisticProvider,
    dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val fastingTracksForTheLastMonth =
        dateTimeProvider.currentTimeFlow().flatMapLatest { currentDateTime ->
            fastingTrackProvider.provideFastingTracksByStartTime(
                currentDateTime.date.minus(
                    1, DateTimeUnit.MONTH
                ).getStartOfDay()..currentDateTime.date.getEndOfDay()
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
        }

    val noteInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val lastThreeFastingTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = fastingTrackProvider.provideLastFastingTracks(
            limitCount = 3
        )
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = statisticProvider.provideFastingStatistic()
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = fastingTracksForTheLastMonth.map { fastingTrackList ->
            fastingTrackList.groupBy {
                it.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
            }.map { entry ->
                entry.key to entry.value.maxBy { it.duration }
            }.map { (fastingDayOfMonth, fastingTrack) ->
                val fastingDuration = fastingTrack.interruptedTime?.let {
                    it - fastingTrack.startTime
                } ?: fastingTrack.duration

                fastingDayOfMonth to fastingDuration.inWholeHours
            }
        }
    )

    val fastingStateLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            dateTimeProvider.currentTimeFlow(),
            currentFastingManager.provideCurrentFastingTrack(),
        ) { currentTime, currentTrack ->
            currentTrack?.let {
                val timeSinceStartOfFasting =
                    currentTime.toInstant(TimeZone.currentSystemDefault()) - currentTrack.startTime
                val isFinished =
                    timeSinceStartOfFasting >= currentTrack.duration || currentTrack.interruptedTime != null

                if (isFinished) {
                    FastingState.Finished(
                        fastingPlan = currentTrack.fastingPlan,
                        isInterrupted = currentTrack.interruptedTime != null,
                        timeLeftInMillis = currentTrack.interruptedTime?.let {
                            it - currentTrack.startTime
                        } ?: run {
                            currentTime.toInstant(TimeZone.currentSystemDefault()) - currentTrack.startTime
                        }
                    )
                } else {
                    FastingState.Fasting(
                        selectedPlan = currentTrack.fastingPlan,
                        durationInMillis = currentTrack.duration,
                        startTimeInMillis = currentTrack.startTime,
                        timeLeftInMillis = timeSinceStartOfFasting,
                    )
                }
            } ?: run {
                FastingState.NotFasting
            }
        }
    )

    val interruptFastingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            currentFastingManager.interruptFasting()
        }
    )

    val finishFastingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            currentFastingManager.clearFasting(note = noteInputController.state.value.input)
            noteInputController.changeInput("")
        }
    )

    sealed class FastingState {
        data class Fasting(
            val selectedPlan: FastingPlan,
            val startTimeInMillis: Instant,
            val durationInMillis: Duration,
            val timeLeftInMillis: Duration
        ) : FastingState()

        object NotFasting : FastingState()

        data class Finished(
            val timeLeftInMillis: Duration,
            val isInterrupted: Boolean,
            val fastingPlan: FastingPlan,
        ) : FastingState()
    }
}