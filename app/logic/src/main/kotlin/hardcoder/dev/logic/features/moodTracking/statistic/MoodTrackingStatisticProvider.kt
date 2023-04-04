package hardcoder.dev.logic.features.moodTracking.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack as MoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackingStatisticProvider(
    private val appDatabase: AppDatabase,
    private val moodTypeProvider: MoodTypeProvider
) {

    fun provideMoodTrackingStatistic() = appDatabase.moodTrackQueries
        .provideAllMoodTracks()
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { moodTracksListDatabase ->
            if (moodTracksListDatabase.isEmpty()) flowOf(null)
            else combine(
                moodTracksListDatabase.map { moodTrack ->
                    provideDrinkTypeById(moodTrack)
                }
            ) { moodTrackArray ->
                val moodTrackList = moodTrackArray.toList()

                val happyMoodCount = moodTrackList.count { it.moodType.positivePercentage >= 80 }

                val neutralMoodCount = moodTrackList.count { it.moodType.positivePercentage >= 60 }

                val notWellMoodCount = moodTrackList.count { it.moodType.positivePercentage >= 40 }

                val badMoodCount = moodTrackList.count { it.moodType.positivePercentage >= 10 }

                val averageMood = moodTrackList.groupingBy {
                    it.moodType
                }.eachCount().maxBy {
                    it.value
                }.key

                MoodTrackingStatistic(
                    happyMoodCount = happyMoodCount,
                    neutralMoodCount = neutralMoodCount,
                    notWellMoodCount = notWellMoodCount,
                    badMoodCount = badMoodCount,
                    averageMoodType = averageMood
                )
            }
        }

    private fun provideDrinkTypeById(moodTrack: MoodTrack): Flow<MoodTrackEntity> {
        return moodTypeProvider.provideMoodTypeByTrackId(moodTrack.moodTypeId).map { moodType ->
            moodTrack.toEntity(moodType!!)
        }
    }

    private fun MoodTrack.toEntity(moodType: MoodType) = MoodTrackEntity(
        id = id,
        moodType = moodType,
        date = date
    )
}