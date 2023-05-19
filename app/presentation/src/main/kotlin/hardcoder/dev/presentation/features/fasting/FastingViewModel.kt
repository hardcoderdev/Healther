package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private val currentTrack = currentFastingManager.provideCurrentFastingTrack().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

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

    private val lastThreeFastingTracks = fastingTrackProvider.provideLastFastingTracks(
        limitCount = 3
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val chartEntries = fastingTracksForTheLastMonth.map { fastingTrackList ->
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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val statisticEntries = statisticProvider.provideFastingStatistic().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val note = MutableStateFlow("")

    val state = combine(
        dateTimeProvider.currentTimeFlow(),
        currentTrack,
        note
    ) { currentTime, currentTrack, note ->
        currentTrack?.let {
            val timeSinceStartOfFasting =
                currentTime.toInstant(TimeZone.currentSystemDefault()) - currentTrack.startTime
            val isFinished =
                timeSinceStartOfFasting >= currentTrack.duration || currentTrack.interruptedTime != null

            if (isFinished) {
                FastingState.Finished(
                    fastingPlan = currentTrack.fastingPlan,
                    isInterrupted = currentTrack.interruptedTime != null,
                    note = note,
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
                    fastingStatistic = statisticEntries.value
                )
            }
        } ?: run {
            FastingState.NotFasting(
                lastFastingTracks = lastThreeFastingTracks.value,
                fastingStatistic = statisticEntries.value,
                chartEntries = chartEntries.value
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FastingState.NotFasting(
            lastFastingTracks = lastThreeFastingTracks.value,
            fastingStatistic = statisticEntries.value,
            chartEntries = chartEntries.value
        )
    )

    fun interruptTrack() {
        viewModelScope.launch {
            currentFastingManager.interruptFasting()
        }
    }

    fun clearFasting() {
        viewModelScope.launch {
            currentFastingManager.clearFasting(note = note.value)
            note.value = ""
        }
    }

    fun updateNote(newNote: String) {
        note.value = newNote
    }

    sealed class FastingState {
        data class Fasting(
            val fastingStatistic: FastingStatistic?,
            val selectedPlan: FastingPlan,
            val startTimeInMillis: Instant,
            val durationInMillis: Duration,
            val timeLeftInMillis: Duration
        ) : FastingState()

        data class NotFasting(
            val fastingStatistic: FastingStatistic?,
            val lastFastingTracks: List<FastingTrack>,
            val chartEntries: List<Pair<Int, Long>>
        ) : FastingState()

        data class Finished(
            val note: String?,
            val timeLeftInMillis: Duration,
            val isInterrupted: Boolean,
            val fastingPlan: FastingPlan,
        ) : FastingState()
    }
}