package hardcoder.dev.logic.features.starvation.track

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.StarvationTrack
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.starvation.StarvationTrack as StarvationTrackEntity

class StarvationTrackProvider(
    private val appDatabase: AppDatabase,
    private val starvationPlanIdMapper: StarvationPlanIdMapper
) {

    fun provideAllStarvationTracks() = appDatabase.starvationTrackQueries
        .selectAllStarvationTracks()
        .asFlow()
        .map {
            it.executeAsList().map { starvationTrackDatabase ->
                starvationTrackDatabase.toEntity()
            }
        }

    fun provideLastStarvationTracks(limitCount: Long) = appDatabase.starvationTrackQueries
        .selectLastStarvationTracks(limitCount)
        .asFlow()
        .map { lastStarvationTracksQuery ->
            lastStarvationTracksQuery.executeAsList().map { starvationTrackDatabase ->
                starvationTrackDatabase.toEntity()
            }
        }

    fun provideStarvationTracksByStartTime(dayRange: LongRange) = appDatabase.starvationTrackQueries
        .selectStarvationTrackByStartTime(dayRange.first, dayRange.last)
        .asFlow()
        .map {
            it.executeAsList().map { starvationTrackDatabase ->
                starvationTrackDatabase.toEntity()
            }
        }

    fun provideStarvationTrackById(id: Int) = appDatabase.starvationTrackQueries
        .selectStarvationTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    fun provideAllCompletedStarvationTracks() = appDatabase.starvationTrackQueries
        .selectAllStarvationTracks()
        .asFlow()
        .map {
            it.executeAsList().filter { starvationTrack ->
                starvationTrack.starvationPlanId != starvationPlanIdMapper.mapToId(
                    StarvationPlan.CUSTOM_PLAN
                ) && starvationTrack.interruptedTimeInMillis == null
            }.map { starvationTrackDatabase ->
                starvationTrackDatabase.toEntity()
            }
        }

    private fun StarvationTrack.toEntity() = StarvationTrackEntity(
        id,
        startTime,
        duration,
        starvationPlanIdMapper.mapToStarvationPlan(starvationPlanId),
        interruptedTimeInMillis
    )
}