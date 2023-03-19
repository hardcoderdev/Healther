package hardcoder.dev.logic.features.starvation.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.starvation.statistic.StarvationDurationStatistic
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import kotlinx.coroutines.flow.map
import kotlin.math.roundToLong

class StarvationStatisticProvider(
    private val appDatabase: AppDatabase,
    private val starvationPlanIdMapper: StarvationPlanIdMapper,
    private val timeUnitMapper: TimeUnitMapper
) {

    fun provideStarvationDuration() = appDatabase.starvationTrackQueries
        .selectStarvationDuration()
        .asFlow()
        .map { selectStarvationDurationQuery ->
            selectStarvationDurationQuery.executeAsOneOrNull()?.let { duration ->
                StarvationDurationStatistic(
                    maximumDurationInHours = duration.MAX?.let {
                        timeUnitMapper.millisToHours(it)
                    },
                    minimumDurationInHours = duration.MIN?.let {
                        timeUnitMapper.millisToHours(it)
                    },
                    averageDurationInHours = duration.AVG?.roundToLong()?.let {
                        timeUnitMapper.millisToHours(it)
                    }
                )
            }
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