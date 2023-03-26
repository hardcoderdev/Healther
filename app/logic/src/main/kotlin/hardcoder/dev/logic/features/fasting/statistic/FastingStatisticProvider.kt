package hardcoder.dev.logic.features.fasting.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.fasting.statistic.FastingDurationStatistic
import hardcoder.dev.extensions.toMillis
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class FastingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val timeUnitMapper: TimeUnitMapper,
    private val fastingTrackProvider: FastingTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {

    fun provideFastingDuration() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map { selectFastingDurationQuery ->
            val fastingTracksDurationList = selectFastingDurationQuery.executeAsList()
                .dropLastWhile {
                    dateTimeProvider.getCurrentTime().toMillis() - it.startTime < it.duration
                }.map {
                    it.duration
                }

            val maximumDurationInHours = fastingTracksDurationList.maxByOrNull { it }
            val minimumDurationInHours = fastingTracksDurationList.minByOrNull { it }
            val averageDurationInHours = fastingTracksDurationList.ifEmpty { null }?.average()

            FastingDurationStatistic(
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

    fun provideFavouriteFastingPlan() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map {
            it.executeAsList().takeIf { list ->
                list.isNotEmpty()
            }?.dropLastWhile { fastingTrack ->
                dateTimeProvider.getCurrentTime()
                    .toMillis() - fastingTrack.startTime < fastingTrack.duration
            }?.groupingBy { fastingTrackDatabase ->
                fastingPlanIdMapper.mapToFastingPlan(fastingTrackDatabase.fastingPlanId)
            }?.eachCount()?.maxByOrNull { entry ->
                entry.value
            }?.key
        }

    fun providePercentageCompleted() = combine(
        fastingTrackProvider.provideAllFastingTracks(),
        fastingTrackProvider.provideAllCompletedFastingTracks()
    ) { allFastingTracks, allCompletedFastingTracks ->
        val allCount = allFastingTracks.dropLastWhile {
            dateTimeProvider.getCurrentTime()
                .toMillis() - it.startTime < it.duration
        }.count()
        val allCompletedCount = allCompletedFastingTracks.dropLastWhile {
            dateTimeProvider.getCurrentTime()
                .toMillis() - it.startTime < it.duration
        }.count()

        if (allCompletedCount != 0 && allCount != 0) {
            (allCompletedCount.toFloat() / allCount.toFloat() * 100).roundToInt()
        } else {
            null
        }
    }
}