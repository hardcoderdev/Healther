package hardcoder.dev.logics.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodTrackDao
import hardcoder.dev.database.entities.features.moodTracking.MoodTrack
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.moodTracking.MoodTrack as MoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackProvider(
    private val moodTrackDao: MoodTrackDao,
    private val moodTypeProvider: MoodTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllMoodTracksByDayRange(dayRange: ClosedRange<Instant>) =
        moodTrackDao.provideMoodTracksByDayRange(dayRange.start, dayRange.endInclusive)
            .flatMapLatest { moodTracksList ->
                if (moodTracksList.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        moodTracksList.map { moodTrack ->
                            moodTypeProvider.provideMoodTypeByTrackId(moodTrack.moodTypeId)
                                .map { moodType ->
                                    moodTrack.toEntity(moodType!!)
                                }
                        },
                    ) {
                        it.toList()
                    }
                }
            }.flowOn(dispatchers.io)

    fun provideById(id: Int) = moodTrackDao
        .provideMoodTrackById(id)
        .flatMapLatest { moodTrack ->
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
        creationDate = creationDate,
    )
}