package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.logic.reward.currency.CurrencyCalculator
import hardcoder.dev.logic.reward.currency.CurrencyCollector
import hardcoder.dev.logic.reward.currency.CurrencyCreator
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.reward.currency.CurrencyType
import hardcoder.dev.logic.reward.currency.RewardableAction
import hardcoder.dev.logic.reward.experience.ExperienceAction
import hardcoder.dev.logic.reward.experience.ExperienceCalculator
import hardcoder.dev.logic.reward.experience.ExperienceCollector
import hardcoder.dev.logic.reward.experience.ExperienceCreator
import hardcoder.dev.math.safeDiv
import kotlin.time.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    currencyCalculator: CurrencyCalculator,
    currencyProvider: CurrencyProvider,
    currencyCreator: CurrencyCreator,
    currencyCollector: CurrencyCollector,
    experienceCreator: ExperienceCreator,
    experienceCalculator: ExperienceCalculator,
    experienceCollector: ExperienceCollector,
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
            fastingTrackList.groupBy {
                it.startTime.toLocalDateTime().dayOfMonth
            }.map { entry ->
                entry.key to entry.value.maxBy { it.duration }
            }.map { (fastingDayOfMonth, fastingTrack) ->
                val fastingDuration = fastingTrack.interruptedTime?.let {
                    it - fastingTrack.startTime
                } ?: fastingTrack.duration

                fastingDayOfMonth to fastingDuration.inWholeHours
            }
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

    val createRewardController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val currentFastingTrack = currentFastingManager.provideCurrentFastingTrack().first()

            // TODO IF INTERRUPTED - GIVE PENALTY ELSE GIVE REWARD BASED ON PROGRESS
            currentFastingTrack?.let {
                experienceCreator.create(
                    date = dateTimeProvider.currentInstant(),
                    isCollected = false,
                    featureType = FeatureType.FASTING,
                    linkedTrackId = currentFastingTrack.id,
                    experiencePointsAmount = experienceCalculator.calculateExperienceFor(
                        experienceAction = ExperienceAction.DailyRateProgress(
                            currentProgressInPercentage = it.fastingProgress,
                        ),
                    ),
                )

                currencyCreator.create(
                    date = dateTimeProvider.currentInstant(),
                    currencyType = CurrencyType.COINS,
                    isCollected = false,
                    featureType = FeatureType.FASTING,
                    linkedTrackId = currentFastingTrack.id,
                    currencyAmount = currencyCalculator.calculateRewardFor(
                        rewardableAction = RewardableAction.DailyRateProgress(
                            currentProgressInPercentage = it.fastingProgress,
                        ),
                    ),
                )
            }
        }
    )

    val interruptFastingController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currentFastingManager.interruptFasting()
        },
    )

    val collectRewardController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currentFastingManager.clearFasting(note = noteInputController.state.value.input)
            noteInputController.changeInput("")
            currencyCollector.collect(featureType = FeatureType.FASTING)
            experienceCollector.collect(featureType = FeatureType.FASTING)
        },
    )

    val rewardLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = currencyProvider.provideRewardsByDate(
            isCollected = false,
            featureType = FeatureType.FASTING,
            dayRange = dateTimeProvider.currentDateRange(),
        ).map { rewardList ->
            rewardList.map {
                it.amount
            }.sum()
        },
    )

    sealed class FastingState {
        object NotFasting : FastingState()

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