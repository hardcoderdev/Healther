package hardcoder.dev.logics.features.moodTracking.statistic

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodTrackDao
import hardcoder.dev.database.entities.features.moodTracking.MoodTrack
import hardcoder.dev.entities.features.moodTracking.MoodTrackingStatistics
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodTrack as MoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingStatisticProvider(
    private val moodTrackDao: MoodTrackDao,
    private val moodTypeProvider: MoodTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideMoodTrackingStatistic() = moodTrackDao
        .provideAllMoodTracks()
        .flatMapLatest { moodTracksListDatabase ->
            if (moodTracksListDatabase.isEmpty()) {
                flowOf(null)
            } else {
                combine(
                    moodTracksListDatabase.map { moodTrack ->
                        provideDrinkTypeById(moodTrack)
                    },
                ) { moodTrackArray ->
                    val moodTrackList = moodTrackArray.toList()

                    val happyMoodCount = moodTrackList.count {
                        it.moodType.positivePercentage in 81..100
                    }

                    val neutralMoodCount = moodTrackList.count {
                        it.moodType.positivePercentage in 61..80
                    }

                    val notWellMoodCount = moodTrackList.count {
                        it.moodType.positivePercentage in 41..60
                    }

                    val badMoodCount = moodTrackList.count {
                        it.moodType.positivePercentage in 10..40
                    }

                    val averageMood = moodTrackList.groupingBy {
                        it.moodType
                    }.eachCount().maxBy {
                        it.value
                    }.key

                    MoodTrackingStatistics(
                        happyMoodCount = happyMoodCount,
                        neutralMoodCount = neutralMoodCount,
                        notWellMoodCount = notWellMoodCount,
                        badMoodCount = badMoodCount,
                        averageMoodType = averageMood,
                    )
                }
            }
        }.flowOn(dispatchers.io)

    private fun provideDrinkTypeById(moodTrack: MoodTrack): Flow<MoodTrackEntity> {
        return moodTypeProvider.provideMoodTypeByTrackId(moodTrack.moodTypeId).map { moodType ->
            moodTrack.toEntity(moodType!!)
        }.flowOn(dispatchers.io)
    }

    private fun MoodTrack.toEntity(moodType: MoodType) = MoodTrackEntity(
        id = id,
        moodType = moodType,
        creationDate = creationDate,
    )
}