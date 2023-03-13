package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.entities.features.starvation.StarvationTrack
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.features.starvation.DateTimeProvider
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanMillisResolver
import hardcoder.dev.logic.features.starvation.statistic.StarvationStatisticProvider
import hardcoder.dev.logic.features.starvation.track.StarvationCurrentIdManager
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import hardcoder.dev.logic.features.starvation.track.StarvationTrackUpdater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

@OptIn(ExperimentalCoroutinesApi::class)
class StarvationViewModel(
    private val starvationCurrentIdManager: StarvationCurrentIdManager,
    private val starvationTrackUpdater: StarvationTrackUpdater,
    starvationTrackProvider: StarvationTrackProvider,
    starvationPlanMillisResolver: StarvationPlanMillisResolver,
    statisticProvider: StarvationStatisticProvider,
    dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val currentTrackId = starvationCurrentIdManager.starvationCurrentTrackId.stateIn(
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

    private val lastThreeStarvationTracks = allStarvationTracks.map {
        it.takeLast(3)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val maximumStarvationDuration = statisticProvider.provideMaximumStarvationTime()
        .map { maximumDuration ->
            maximumDuration?.let {
                starvationPlanMillisResolver.resolve(it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val averageStarvationDuration =
        statisticProvider.provideAverageStarvationTime().map { averageDuration ->
            averageDuration?.let {
                starvationPlanMillisResolver.resolve(it.roundToLong())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val minimumStarvationDuration =
        statisticProvider.provideMinimumStarvationTime().map { minimumDuration ->
            minimumDuration?.let {
                starvationPlanMillisResolver.resolve(it)
            }
        }.stateIn(
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
        maximumStarvationDuration,
        averageStarvationDuration,
        minimumStarvationDuration,
        percentageCompleted,
    ) { maximumStarvationDuration, averageStarvationDuration,
        minimumStarvationDuration, percentageCompleted ->
        listOf(
            0 to (maximumStarvationDuration?.toString()),
            1 to (minimumStarvationDuration?.toString()),
            2 to (averageStarvationDuration?.toString()),
            3 to (percentageCompleted?.toString())
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = listOf(0 to "")
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
                    statistic = Statistic(
                        favoritePlan = favoriteStarvationPlan.value,
                        statisticEntries = statisticEntries.value
                    ),
                )
            }
        } ?: run {
            StarvationState.NotStarving.State(
                lastThreeStarvationTracks = lastThreeStarvationTracks.value,
                statistic = Statistic(
                    favoritePlan = favoriteStarvationPlan.value,
                    statisticEntries = statisticEntries.value
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StarvationState.NotStarving.State(
            lastThreeStarvationTracks = emptyList(),
            statistic = Statistic(
                favoritePlan = favoriteStarvationPlan.value,
                statisticEntries = statisticEntries.value
            )
        )
    )

    fun interruptTrack() {
        viewModelScope.launch {
            starvationTrackUpdater.interruptStarvation(
                trackId = currentTrackId.value,
                duration = System.currentTimeMillis() - currentTrack.value!!.startTime
            )
        }
    }

    fun clearCurrentTrack() {
        viewModelScope.launch {
            starvationCurrentIdManager.setCurrentId(null)
        }
    }

    sealed class StarvationState {
        data class Starving(val state: State) : StarvationState() {
            data class State(
                val statistic: Statistic,
                val selectedPlan: StarvationPlan,
                val startTimeInMillis: Long,
                val durationInMillis: Long,
                val timeLeftInMillis: Long
            )
        }

        data class NotStarving(val state: State) : StarvationState() {
            data class State(
                val statistic: Statistic,
                val lastThreeStarvationTracks: List<StarvationTrack>
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

    data class Statistic(
        val statisticEntries: List<Pair<Int, String?>>,
        val favoritePlan: StarvationPlan?
    )
}