package hardcoder.dev.logic.features.fasting.track

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.fasting.FastingTrack as FastingTrackEntity

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

    fun provideFastingTracksByStartTime(dayRange: LongRange) = appDatabase.fastingTrackQueries
        .provideFastingTrackByStartTime(dayRange.first, dayRange.last)
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
                fastingTrack.interruptedTimeInMillis == null
            }.map { fastingTrackDatabase ->
                fastingTrackDatabase.toEntity()
            }
        }

    private fun FastingTrack.toEntity() = FastingTrackEntity(
        id,
        startTime,
        duration,
        fastingPlanIdMapper.mapToFastingPlan(fastingPlanId),
        interruptedTimeInMillis
    )
}