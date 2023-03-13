package hardcoder.dev.logic.features.starvation.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import kotlinx.coroutines.flow.map

class StarvationStatisticProvider(
    private val appDatabase: AppDatabase,
    private val starvationPlanIdMapper: StarvationPlanIdMapper
) {

    fun provideMaximumStarvationTime() = appDatabase.starvationTrackQueries
        .selectStarvationDuration()
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.MAX
        }

    fun provideAverageStarvationTime() = appDatabase.starvationTrackQueries
        .selectStarvationDuration()
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.AVG
        }

    fun provideMinimumStarvationTime() = appDatabase.starvationTrackQueries
        .selectStarvationDuration()
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.MIN
        }

    fun provideFavouriteStarvationPlan() = appDatabase.starvationTrackQueries
        .selectAllStarvationTracks()
        .asFlow()
        .map {
            it.executeAsList().takeIf { list ->
                list.isNotEmpty()
            }?.groupingBy { starvationTrackDatabase ->
                starvationPlanIdMapper.mapToStarvationPlan(starvationTrackDatabase.starvationPlanId)
            }?.eachCount()?.maxBy { entry ->
                entry.value
            }?.key
        }
}