package hardcoder.dev.logics.features.foodTracking.statistic

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.DateTimeProvider

class FoodTrackingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

//    fun provideFastingStatistic(): Flow<FastingStatistic?> = combine(
//        provideFastingDuration(),
//        provideFavouriteFastingPlan(),
//    ) { fastingDuration, favouritePlan ->
//        if (fastingDuration == null && favouritePlan == null) {
//            null
//        } else {
//            FastingStatistic(duration = fastingDuration)
//        }
//    }.flowOn(dispatchers.default)
//
//    private fun provideFastingDuration() = appDatabase.fastingTrackQueries
//        .provideAllFastingTracks()
//        .asFlow()
//        .flowOn(dispatchers.io)
//        .map { selectFastingDurationQuery ->
//            val fastingTracksDurationList = selectFastingDurationQuery.executeAsList()
//                .dropLastWhile {
//                    (dateTimeProvider.currentInstant() - it.dateTime).inWholeMilliseconds < it.duration.hours.inWholeMilliseconds
//                }.map {
//                    it.duration
//                }
//
//            val maximumDurationInHours = fastingTracksDurationList.maxByOrNull { it }
//            val minimumDurationInHours = fastingTracksDurationList.minByOrNull { it }
//            val averageDurationInHours = fastingTracksDurationList.safeAverage()
//
//            if (fastingTracksDurationList.isNotEmpty()) {
//                FastingDurationStatistic(
//                    maximumDurationInHours = maximumDurationInHours?.hours?.inWholeHours,
//                    minimumDurationInHours = minimumDurationInHours?.hours?.inWholeHours,
//                    averageDurationInHours = averageDurationInHours.roundToLong().hours.inWholeHours,
//                )
//            } else {
//                null
//            }
//        }.flowOn(dispatchers.default)
//
//    private fun provideFavouriteFastingPlan() = appDatabase.fastingTrackQueries
//        .provideAllFastingTracks()
//        .asFlow()
//        .flowOn(dispatchers.io)
//        .map {
//            it.executeAsList().takeIf { list ->
//                list.isNotEmpty()
//            }?.dropLastWhile { fastingTrack ->
//                (dateTimeProvider.currentInstant() - fastingTrack.startTime).inWholeMilliseconds <
//                    fastingTrack.duration.hours.inWholeMilliseconds
//            }?.groupingBy { fastingTrackDatabase ->
//                fastingPlanIdMapper.mapToFastingPlan(fastingTrackDatabase.fastingPlanId)
//            }?.eachCount()?.maxByOrNull { entry ->
//                entry.value
//            }?.key
//        }.flowOn(dispatchers.default)
}