package hardcoder.dev.logic.features.starvation.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.starvation.statistic.StarvationDurationStatistic
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.features.starvation.DateTimeProvider
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.roundToLong

class StarvationStatisticProvider(
    private val appDatabase: AppDatabase,
    private val starvationPlanIdMapper: StarvationPlanIdMapper,
    private val timeUnitMapper: TimeUnitMapper,
    private val starvationTrackProvider: StarvationTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {

    fun provideStarvationDuration() = appDatabase.starvationTrackQueries
        .selectAllStarvationTracks()
        .asFlow()
        .map { selectStarvationDurationQuery ->
            val starvationTracksDurationList = selectStarvationDurationQuery.executeAsList()
                .dropLastWhile {
                    dateTimeProvider.getCurrentTime().toMillis() - it.startTime < it.duration
                }.map {
                    it.duration
                }

            val maximumDurationInHours = starvationTracksDurationList.maxByOrNull { it }
            val minimumDurationInHours = starvationTracksDurationList.minByOrNull { it }
            val averageDurationInHours = starvationTracksDurationList.ifEmpty { null }?.average()

            StarvationDurationStatistic(
                maximumDurationInHours = maximumDurationInHours?.let {
                    timeUnitMapper.millisToHours(it)
                },
                minimumDurationInHours = minimumDurationInHours?.let {
                    timeUnitMapper.millisToHours(it)
                },
                averageDurationInHours = averageDurationInHours?.roundToLong()?.let {
                    timeUnitMapper.millisToHours(it)
                }
            )
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

    fun providePercentageCompleted() = combine(
        starvationTrackProvider.provideAllStarvationTracks(),
        starvationTrackProvider.provideAllCompletedStarvationTracks()
    ) { allStarvationTracks, allCompletedStarvationTracks ->
        if (allCompletedStarvationTracks.isNotEmpty()) {
            (allStarvationTracks.count() / allCompletedStarvationTracks.count()) * 10
        } else {
            null
        }
    }
}