package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.entities.features.starvation.StarvationTrack
import hardcoder.dev.entities.features.starvation.statistic.StarvationStatistic
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.features.starvation.DateTimeProvider
import hardcoder.dev.logic.features.starvation.statistic.StarvationStatisticProvider
import hardcoder.dev.logic.features.starvation.track.StarvationCurrentTrackManager
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StarvationViewModel(
    private val starvationCurrentTrackManager: StarvationCurrentTrackManager,
    starvationTrackProvider: StarvationTrackProvider,
    statisticProvider: StarvationStatisticProvider,
    dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val currentTrackId = starvationCurrentTrackManager.starvationCurrentTrackId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val selectedPlan = currentTrackId.flatMapLatest {
        starvationTrackProvider.provideStarvationTrackById(currentTrackId.value).map {
            it?.starvationPlan
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val currentTrack = currentTrackId.flatMapLatest {
        starvationTrackProvider.provideStarvationTrackById(currentTrackId.value)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val allStarvationTracks = starvationTrackProvider.provideAllStarvationTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val allCompletedStarvationTracks =
        starvationTrackProvider.provideAllCompletedStarvationTracks()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
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

    private val percentageCompleted = combine(
        allStarvationTracks,
        allCompletedStarvationTracks
    ) { allStarvationTracks, allCompletedStarvationTracks ->
        if (allCompletedStarvationTracks.isNotEmpty()) {
            (allStarvationTracks.count() / allCompletedStarvationTracks.count()) * 10
        } else {
            null
        }
    }.stateIn(
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
        selectedPlan,
    ) { currentTime, currentTrack, selectedPlan ->
        currentTrack?.let {
            val timeSinceStartOfStarvation = currentTime.toMillis() - currentTrack.startTime
            val isFinished =
                timeSinceStartOfStarvation >= currentTrack.duration || currentTrack.interruptedTime != null

            if (isFinished) {
                StarvationState.Finished.State(
                    starvationPlan = currentTrack.starvationPlan,
                    interruptedMillis = currentTrack.interruptedTime,
                    startTimeInMillis = currentTrack.startTime
                )
            } else {
                StarvationState.Starving.State(
                    selectedPlan = requireNotNull(selectedPlan),
                    durationInMillis = currentTrack.duration,
                    startTimeInMillis = currentTrack.startTime,
                    timeLeftInMillis = timeSinceStartOfStarvation,
                    starvationStatistic = statisticEntries.value
                )
            }
        } ?: run {
            StarvationState.NotStarving.State(
                lastStarvationTracks = lastThreeStarvationTracks.value,
                starvationStatistic = statisticEntries.value
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StarvationState.NotStarving.State(
            lastStarvationTracks = emptyList(),
            starvationStatistic = statisticEntries.value
        )
    )

    fun interruptTrack() {
        viewModelScope.launch {
            starvationCurrentTrackManager.interruptStarvation(
                duration = System.currentTimeMillis() - currentTrack.value!!.startTime
            )
        }
    }

    fun clearStarvation() {
        viewModelScope.launch {
            starvationCurrentTrackManager.clearStarvation()
        }
    }

    sealed class StarvationState {
        data class Starving(val state: State) : StarvationState() {
            data class State(
                val starvationStatistic: StarvationStatistic,
                val selectedPlan: StarvationPlan,
                val startTimeInMillis: Long,
                val durationInMillis: Long,
                val timeLeftInMillis: Long
            )
        }

        data class NotStarving(val state: State) : StarvationState() {
            data class State(
                val starvationStatistic: StarvationStatistic,
                val lastStarvationTracks: List<StarvationTrack>
            )
        }

        data class Finished(val state: State) : StarvationState() {
            data class State(
                val startTimeInMillis: Long,
                val starvationPlan: StarvationPlan,
                val interruptedMillis: Long?
            )
        }
    }
}