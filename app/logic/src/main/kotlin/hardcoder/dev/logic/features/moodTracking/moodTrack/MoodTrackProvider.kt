package hardcoder.dev.logic.features.moodTracking.moodTrack

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack as MoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackProvider(
    private val appDatabase: AppDatabase,
    private val moodTypeProvider: MoodTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun provideAllMoodTracksByDayRange(dayRange: ClosedRange<Instant>) =
        appDatabase.moodTrackQueries
            .provideMoodTracksByDayRange(dayRange.start, dayRange.endInclusive)
            .asFlow()
            .map {
                it.executeAsList()
            }.flatMapLatest { moodTracksList ->
                if (moodTracksList.isEmpty()) flowOf(emptyList())
                else combine(
                    moodTracksList.map { moodTrack ->
                        moodTypeProvider.provideMoodTypeByTrackId(moodTrack.moodTypeId)
                            .map { moodType ->
                                moodTrack.toEntity(moodType!!)
                            }
                    }
                ) {
                    it.toList()
                }
            }.flowOn(dispatchers.io)

    fun provideById(id: Int) = appDatabase.moodTrackQueries
        .provideMoodTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()
        }.flatMapLatest { moodTrack ->
            if (moodTrack == null) {
                flowOf(null)
            } else {
                moodTypeProvider.provideMoodTypeByTrackId(moodTrack.moodTypeId).map { moodType ->
                    moodTrack.toEntity(moodType!!)
                }
            }
        }.flowOn(dispatchers.io)

    private fun MoodTrack.toEntity(moodType: MoodType) = MoodTrackEntity(
        id = id,
        moodType = moodType,
        date = date
    )
}