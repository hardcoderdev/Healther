package hardcoder.dev.logics.features.moodTracking.moodWithActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.moodTracking.MoodWithActivityDao
import hardcoder.dev.entities.features.moodTracking.MoodWithActivities
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Instant

class MoodWithActivitiesProvider(
    private val moodWithActivityDao: MoodWithActivityDao,
    private val moodActivityProvider: MoodActivityProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideMoodWithActivityList(dayRange: ClosedRange<Instant>) = moodWithActivityDao.provideMoodTracksWithActivitiesByDayRange(
        startTime = dayRange.start,
        endTime = dayRange.endInclusive,
    ).mapItems {
        MoodWithActivities(
            moodTrack = moodTrackProvider.provideById(it.moodTrack.id).firstNotNull(),
            moodActivityList = it.moodActivityList.map { moodActivity ->
                moodActivityProvider.provideActivityById(moodActivity.id).firstNotNull()
            },
        )
    }

    fun provideMoodWithActivitiesById(moodTrackId: Int) = moodWithActivityDao
        .provideMoodTrackWithActivitiesByMoodTrackId(moodTrackId)
        .flatMapLatest { moodTrackWithActivities ->
            if (moodTrackWithActivities == null) flowOf(null)
            else {
                flowOf(
                    MoodWithActivities(
                        moodTrack = moodTrackProvider.provideById(moodTrackWithActivities.moodTrack.id).firstNotNull(),
                        moodActivityList = moodTrackWithActivities.moodActivityList.map { moodActivity ->
                            moodActivityProvider.provideActivityById(moodActivity.id).firstNotNull()
                        },
                    ),
                )
            }
        }.flowOn(dispatchers.io)
}