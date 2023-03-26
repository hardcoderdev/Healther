package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.entities.features.fasting.FastingTrack
import hardcoder.dev.entities.features.fasting.statistic.FastingStatistic
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val lastThreeFastingTracks = fastingTrackProvider.provideLastFastingTracks(
        limitCount = 3
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val fastingDurationStatistic = statisticProvider.provideFastingDuration().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val favoriteFastingPlan = statisticProvider.provideFavouriteFastingPlan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val percentageCompleted = statisticProvider.providePercentageCompleted().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val statisticEntries = combine(
        fastingDurationStatistic,
        percentageCompleted,
        favoriteFastingPlan
    ) { fastingDurationStatistic, percentageCompleted, favoriteFastingPlan ->
        FastingStatistic(
            fastingDurationStatistic,
            percentageCompleted = percentageCompleted,
            favouritePlan = favoriteFastingPlan
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FastingStatistic(
            fastingDurationStatistic = null,
            percentageCompleted = null,
            favouritePlan = null
        )
    )

    val state = combine(
        dateTimeProvider.currentTimeFlow(),
        currentTrack,
    ) { currentTime, currentTrack ->
        currentTrack?.let {
            val timeSinceStartOfFasting = currentTime.toMillis() - currentTrack.startTime
            val isFinished = timeSinceStartOfFasting >= currentTrack.duration || currentTrack.interruptedTime != null

            if (isFinished) {
                FastingState.Finished(
                    fastingPlan = currentTrack.fastingPlan,
                    isInterrupted = currentTrack.interruptedTime != null,
                    timeLeftInMillis = currentTrack.interruptedTime?.let {
                        it - currentTrack.startTime
                    } ?: run {
                        currentTime.toMillis() - currentTrack.startTime
                    }
                )
            } else {
                FastingState.Starving(
                    selectedPlan = currentTrack.fastingPlan,
                    durationInMillis = currentTrack.duration,
                    startTimeInMillis = currentTrack.startTime,
                    timeLeftInMillis = timeSinceStartOfFasting,
                    fastingStatistic = statisticEntries.value
                )
            }
        } ?: run {
            FastingState.NotStarving(
                lastFastingTracks = lastThreeFastingTracks.value,
                fastingStatistic = statisticEntries.value
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FastingState.NotStarving(
            lastFastingTracks = emptyList(),
            fastingStatistic = statisticEntries.value
        )
    )

    fun interruptTrack() {
        viewModelScope.launch {
            currentFastingManager.interruptFasting(
                duration = System.currentTimeMillis() - currentTrack.value!!.startTime
            )
        }
    }

    fun clearFasting() {
        viewModelScope.launch {
            currentFastingManager.clearFasting()
        }
    }

    sealed class FastingState {
        data class Starving(
            val fastingStatistic: FastingStatistic,
            val selectedPlan: FastingPlan,
            val startTimeInMillis: Long,
            val durationInMillis: Long,
            val timeLeftInMillis: Long
        ) : FastingState()

        data class NotStarving(
            val fastingStatistic: FastingStatistic,
            val lastFastingTracks: List<FastingTrack>
        ) : FastingState()

        data class Finished(
            val timeLeftInMillis: Long,
            val isInterrupted: Boolean,
            val fastingPlan: FastingPlan,
        ) : FastingState()
    }
}