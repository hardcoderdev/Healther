package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.entities.features.starvation.StarvationTrack
import hardcoder.dev.entities.features.starvation.statistic.StarvationStatistic
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.features.starvation.DateTimeProvider
import hardcoder.dev.logic.features.starvation.statistic.StarvationStatisticProvider
import hardcoder.dev.logic.features.starvation.track.CurrentStarvationManager
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StarvationViewModel(
    private val currentStarvationManager: CurrentStarvationManager,
    starvationTrackProvider: StarvationTrackProvider,
    statisticProvider: StarvationStatisticProvider,
    dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val currentTrack =
        currentStarvationManager.provideCurrentStarvationTrack().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val lastThreeStarvationTracks = starvationTrackProvider.provideLastStarvationTracks(
        limitCount = 3
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val starvationDurationStatistic = statisticProvider.provideStarvationDuration().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val favoriteStarvationPlan = statisticProvider.provideFavouriteStarvationPlan().stateIn(
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
        starvationDurationStatistic,
        percentageCompleted,
        favoriteStarvationPlan
    ) { starvationDurationStatistic, percentageCompleted, favoriteStarvationPlan ->
        StarvationStatistic(
            starvationDurationStatistic,
            percentageCompleted = percentageCompleted,
            favouritePlan = favoriteStarvationPlan
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StarvationStatistic(
            starvationDurationStatistic = null,
            percentageCompleted = null,
            favouritePlan = null
        )
    )

    val state = combine(
        dateTimeProvider.currentTimeFlow(),
        currentTrack,
    ) { currentTime, currentTrack ->
        currentTrack?.let {
            val timeSinceStartOfStarvation = currentTime.toMillis() - currentTrack.startTime
            val isFinished =
                timeSinceStartOfStarvation >= currentTrack.duration || currentTrack.interruptedTime != null

            if (isFinished) {
                StarvationState.Finished(
                    starvationPlan = currentTrack.starvationPlan,
                    isInterrupted = currentTrack.interruptedTime != null,
                    timeLeftInMillis = currentTrack.interruptedTime?.let {
                        it - currentTrack.startTime
                    } ?: run {
                        currentTime.toMillis() - currentTrack.startTime
                    }
                )
            } else {
                StarvationState.Starving(
                    selectedPlan = currentTrack.starvationPlan,
                    durationInMillis = currentTrack.duration,
                    startTimeInMillis = currentTrack.startTime,
                    timeLeftInMillis = timeSinceStartOfStarvation,
                    starvationStatistic = statisticEntries.value
                )
            }
        } ?: run {
            StarvationState.NotStarving(
                lastStarvationTracks = lastThreeStarvationTracks.value,
                starvationStatistic = statisticEntries.value
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StarvationState.NotStarving(
            lastStarvationTracks = emptyList(),
            starvationStatistic = statisticEntries.value
        )
    )

    fun interruptTrack() {
        viewModelScope.launch {
            currentStarvationManager.interruptStarvation(
                duration = System.currentTimeMillis() - currentTrack.value!!.startTime
            )
        }
    }

    fun clearStarvation() {
        viewModelScope.launch {
            currentStarvationManager.clearStarvation()
        }
    }

    sealed class StarvationState {
        data class Starving(
            val starvationStatistic: StarvationStatistic,
            val selectedPlan: StarvationPlan,
            val startTimeInMillis: Long,
            val durationInMillis: Long,
            val timeLeftInMillis: Long
        ) : StarvationState()

        data class NotStarving(
            val starvationStatistic: StarvationStatistic,
            val lastStarvationTracks: List<StarvationTrack>
        ) : StarvationState()

        data class Finished(
            val timeLeftInMillis: Long,
            val isInterrupted: Boolean,
            val starvationPlan: StarvationPlan,
        ) : StarvationState()
    }
}