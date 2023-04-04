package hardcoder.dev.logic.features.fasting.track

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import hardcoder.dev.logic.features.fasting.track.FastingTrack as FastingTrackEntity

class FastingTrackProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper
) {

    fun provideAllFastingTracks() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map {
            it.executeAsList().map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }

    fun provideLastFastingTracks(limitCount: Long) = appDatabase.fastingTrackQueries
        .provideLastFastingTracks(limitCount)
        .asFlow()
        .map { lastFastingTracksQuery ->
            lastFastingTracksQuery.executeAsList().map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }

    fun provideFastingTracksByStartTime(dayRange: ClosedRange<Instant>) = appDatabase.fastingTrackQueries
        .provideFastingTrackByStartTime(dayRange.start, dayRange.endInclusive)
        .asFlow()
        .map {
            it.executeAsList().map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }

    fun provideFastingTrackById(id: Int) = appDatabase.fastingTrackQueries
        .provideFastingTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    fun provideAllCompletedFastingTracks() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map {
            it.executeAsList().filter { fastingTrack ->
                fastingTrack.interruptedTime == null
            }.map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }

    private fun FastingTrack.toEntity() = FastingTrackEntity(
        id = id,
        startTime = startTime,
        duration = duration.toDuration(DurationUnit.HOURS),
        fastingPlan = fastingPlanIdMapper.mapToFastingPlan(fastingPlanId),
        interruptedTime = interruptedTime
    )
}