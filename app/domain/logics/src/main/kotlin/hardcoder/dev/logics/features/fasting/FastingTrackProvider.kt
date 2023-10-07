package hardcoder.dev.logic.features.fasting

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.mappers.features.fasting.FastingPlanIdMapper
import hardcoder.dev.sqldelight.asFlowOfList
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.fasting.FastingTrack as FastingTrackEntity

class FastingTrackProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideLastFastingTracks(limitCount: Long) = appDatabase.fastingTrackQueries
        .provideLastFastingTracks(limitCount)
        .asFlowOfList(dispatchers.io) { fastingTrackDatabase ->
            fastingTrackDatabase.toEntity()
        }

    fun provideFastingTracksByStartTime(dayRange: ClosedRange<Instant>) = appDatabase.fastingTrackQueries
        .provideFastingTrackByStartTime(dayRange.start, dayRange.endInclusive)
        .asFlowOfList(dispatchers.io) { fastingTrackDatabase ->
            fastingTrackDatabase.toEntity()
        }

    fun provideFastingTrackById(id: Int) = appDatabase.fastingTrackQueries
        .provideFastingTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun FastingTrack.toEntity() = FastingTrackEntity(
        id = id,
        startTime = startTime,
        duration = duration.toDuration(DurationUnit.HOURS),
        fastingPlan = fastingPlanIdMapper.mapToFastingPlan(fastingPlanId),
        interruptedTime = interruptedTime,
        fastingProgress = 100f,
    )
}