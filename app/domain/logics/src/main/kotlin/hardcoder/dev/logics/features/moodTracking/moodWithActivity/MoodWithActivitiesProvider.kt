package hardcoder.dev.logics.features.moodTracking.moodWithActivity

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodWithActivityDao
import hardcoder.dev.database.entities.features.moodTracking.MoodTrackWithActivities
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MoodWithActivitiesProvider(
    private val moodWithActivityDao: MoodWithActivityDao,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodActivityProvider: MoodActivityProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideMoodWithActivityList(dayRange: ClosedRange<Instant>): Flow<List<MoodTrackWithActivities>> {
        return moodWithActivityDao.provideMoodTracksWithActivitiesByDayRange(
            startTime = dayRange.start,
            endTime = dayRange.endInclusive,
        )
    }

    fun provideActivityListByMoodTrackId(id: Int) = appDatabase.moodWithActivityQueries
        .provideAllMoodWithActivitiesByMoodTrackId(id)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { moodWithActivities ->
            if (moodWithActivities.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    moodWithActivities.map {
                        moodActivityProvider.provideActivityById(it.activityId)
                    },
                ) { activities ->
                    activities.toList().filterNotNull()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideMoodWithActivitiesById(id: Int) = moodWithActivityDao.provideMoodTracksWithActivitiesByDayRange()
}