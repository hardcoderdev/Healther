package hardcoder.dev.logic.features.moodTracking.moodTrack

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodTrack
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.moodTracking.MoodTrack as MoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackProvider(
    private val appDatabase: AppDatabase,
    private val moodTypeProvider: MoodTypeProvider
) {

    fun provideAllMoodTracksByDayRange(dayRange: LongRange) =
        appDatabase.moodTrackQueries
            .provideMoodTracksByDayRange(dayRange.first, dayRange.last)
            .asFlow()
            .map {
                it.executeAsList()
            }.flatMapLatest { moodTracksList ->
                if (moodTracksList.isEmpty()) flowOf(emptyList())
                else combine(
                    moodTracksList.map { waterTrack ->
                        provideDrinkTypeById(waterTrack)
                    }
                ) {
                    it.toList()
                }
            }

    fun provideById(id: Int) = appDatabase.moodTrackQueries
        .provideMoodTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()
        }.flatMapLatest {
            if (it == null) {
                flowOf(null)
            } else {
                provideDrinkTypeById(it)
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
        date = Instant.fromEpochMilliseconds(creationTime)
    )
}