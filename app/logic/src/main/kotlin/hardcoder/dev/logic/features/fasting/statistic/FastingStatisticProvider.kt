package hardcoder.dev.logic.features.fasting.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.hours

class FastingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val fastingTrackProvider: FastingTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun provideFastingStatistic(): Flow<FastingStatistic?> = combine(
        provideFastingDuration(),
        provideFavouriteFastingPlan(),
        providePercentageCompleted()
    ) { fastingDuration, favouritePlan, percentageCompleted ->
        if (fastingDuration == null && favouritePlan == null && percentageCompleted == null) null
        else {
            FastingStatistic(
                duration = fastingDuration,
                favouritePlan = favouritePlan,
                percentageCompleted = percentageCompleted
            )
        }
    }.flowOn(dispatchers.io)

    private fun provideFastingDuration() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map { selectFastingDurationQuery ->
            val fastingTracksDurationList = selectFastingDurationQuery.executeAsList()
                .dropLastWhile {
                    dateTimeProvider.getCurrentTime()
                        .toInstant(TimeZone.currentSystemDefault()) - it.startTime < it.duration.hours
                }.map {
                    it.duration
                }

            val maximumDurationInHours = fastingTracksDurationList.ifEmpty { null }?.maxByOrNull { it }
            val minimumDurationInHours = fastingTracksDurationList.ifEmpty { null }?.minByOrNull { it }
            val averageDurationInHours = fastingTracksDurationList.ifEmpty { null }?.average()

            if (fastingTracksDurationList.isNotEmpty()) {
                FastingDurationStatistic(
                    maximumDurationInHours = maximumDurationInHours?.hours?.inWholeHours,
                    minimumDurationInHours = minimumDurationInHours?.hours?.inWholeHours,
                    averageDurationInHours = averageDurationInHours?.roundToLong()?.hours?.inWholeHours
                )
            } else {
                null
            }
        }.flowOn(dispatchers.io)

    private fun provideFavouriteFastingPlan() = appDatabase.fastingTrackQueries
        .provideAllFastingTracks()
        .asFlow()
        .map {
            it.executeAsList().takeIf { list ->
                list.isNotEmpty()
            }?.dropLastWhile { fastingTrack ->
                dateTimeProvider.getCurrentTime()
                    .toInstant(TimeZone.currentSystemDefault()) - fastingTrack.startTime < fastingTrack.duration.hours
            }?.groupingBy { fastingTrackDatabase ->
                fastingPlanIdMapper.mapToFastingPlan(fastingTrackDatabase.fastingPlanId)
            }?.eachCount()?.maxByOrNull { entry ->
                entry.value
            }?.key
        }.flowOn(dispatchers.io)

    private fun providePercentageCompleted() = combine(
        fastingTrackProvider.provideAllFastingTracks(),
        fastingTrackProvider.provideAllCompletedFastingTracks()
    ) { allFastingTracks, allCompletedFastingTracks ->
        val allCount = allFastingTracks.dropLastWhile {
            dateTimeProvider.getCurrentTime()
                .toInstant(TimeZone.currentSystemDefault()) - it.startTime < it.duration
        }.count()
        val allCompletedCount = allCompletedFastingTracks.dropLastWhile {
            dateTimeProvider.getCurrentTime()
                .toInstant(TimeZone.currentSystemDefault()) - it.startTime < it.duration
        }.count()

        if (allCompletedCount != 0 && allCount != 0) {
            (allCompletedCount.toFloat() / allCount.toFloat() * 100).roundToInt()
        } else {
            null
        }
    }.flowOn(dispatchers.io)
}