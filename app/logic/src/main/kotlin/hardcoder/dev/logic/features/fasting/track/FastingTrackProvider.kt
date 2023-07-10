package hardcoder.dev.logic.features.fasting.track

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.math.safeDiv
import hardcoder.dev.sqldelight.asFlowOfList
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.fasting.track.FastingTrack as FastingTrackEntity

class FastingTrackProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllFastingTracks() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlowOfList(dispatchers.io) { fastingTrackDatabase ->
            fastingTrackDatabase.toEntity()
        }

    fun provideLastFastingTracks(limitCount: Long) = appDatabase.fastingTrackQueries
        .provideLastFastingTracks(limitCount)
        .asFlowOfList(dispatchers.io) { fastingTrackDatabase ->
            fastingTrackDatabase.toEntity()
        }

    fun provideFastingTracksByStartTime(dayRange: ClosedRange<Instant>) =
        appDatabase.fastingTrackQueries
            .provideFastingTrackByStartTime(dayRange.start, dayRange.endInclusive)
            .asFlowOfList(dispatchers.io) { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }

    fun provideFastingTrackById(id: Int) = appDatabase.fastingTrackQueries
        .provideFastingTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    fun provideAllCompletedFastingTracks() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map {
            it.executeAsList().filter { fastingTrack ->
                fastingTrack.interruptedTime == null
            }.map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }.flowOn(dispatchers.io)

    private fun FastingTrack.toEntity(): FastingTrackEntity {
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
        )
    }
}