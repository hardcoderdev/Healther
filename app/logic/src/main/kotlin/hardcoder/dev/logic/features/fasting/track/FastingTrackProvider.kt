package hardcoder.dev.logic.features.fasting.track

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.math.safeDiv
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.fasting.track.FastingTrack as FastingTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class FastingTrackProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val currencyProvider: CurrencyProvider,
) {

    fun provideLastFastingTracks(limitCount: Long) = appDatabase.fastingTrackQueries
        .provideLastFastingTracks(limitCount)
        .provideFastingTracks()

    fun provideFastingTracksByStartTime(dayRange: ClosedRange<Instant>) = appDatabase.fastingTrackQueries
        .provideFastingTrackByStartTime(dayRange.start, dayRange.endInclusive)
        .provideFastingTracks()

    fun provideFastingTrackById(id: Int) = appDatabase.fastingTrackQueries
        .provideFastingTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull() }
        .flatMapLatest { fastingTrack ->
            combine(
                currencyProvider.isTrackCollected(
                    featureType = FeatureType.FASTING,
                    linkedTrackId = id,
                ),
            ) {
                fastingTrack?.toEntity(isRewardCollected = it[0])
            }
        }
        .flowOn(dispatchers.io)

    private fun Query<FastingTrack>.provideFastingTracks() = asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { fastingTrackList ->
            if (fastingTrackList.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    fastingTrackList.map { fastingTrack ->
                        combine(
                            currencyProvider.isTrackCollected(
                                featureType = FeatureType.FASTING,
                                linkedTrackId = fastingTrack.id,
                            ),
                        ) { isRewardCollected ->
                            fastingTrack.toEntity(isRewardCollected = isRewardCollected[0])
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }

    private fun FastingTrack.toEntity(isRewardCollected: Boolean): FastingTrackEntity {
        val fastingTrackDurationInHours = duration.toDuration(DurationUnit.HOURS)
        val fastingProgressInMillis = interruptedTime?.let {
            it - startTime
        } ?: run {
            fastingTrackDurationInHours
        }

        return FastingTrackEntity(
            id = id,
            startTime = startTime,
            duration = duration.toDuration(DurationUnit.HOURS),
            fastingPlan = fastingPlanIdMapper.mapToFastingPlan(fastingPlanId),
            interruptedTime = interruptedTime,
            fastingProgress = fastingProgressInMillis.inWholeMilliseconds safeDiv fastingTrackDurationInHours.inWholeMilliseconds,
            isRewardCollected = isRewardCollected,
        )
    }
}