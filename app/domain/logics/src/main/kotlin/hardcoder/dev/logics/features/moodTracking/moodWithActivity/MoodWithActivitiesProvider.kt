package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.moodTracking.MoodWithActivities
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
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
    private val appDatabase: AppDatabase,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodActivityProvider: MoodActivityProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideMoodWithActivityList(dayRange: ClosedRange<Instant>): Flow<List<MoodWithActivities>> {
        return moodTrackProvider.provideAllMoodTracksByDayRange(dayRange = dayRange)
            .flatMapLatest { tracks ->
                if (tracks.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        tracks.map { moodTrack ->
                            appDatabase.moodWithActivityQueries.provideAllMoodWithActivitiesByMoodTrackId(
                                moodTrack.id,
                            )
                                .asFlow()
                                .map { it.executeAsList() }
                                .flatMapLatest { moodWithActivities ->
                                    if (moodWithActivities.isEmpty()) {
                                        flowOf(
                                            MoodWithActivities(
                                                moodTrack = moodTrack,
                                                moodActivityList = emptyList(),
                                            ),
                                        )
                                    } else {
                                        combine(
                                            moodWithActivities.map {
                                                moodActivityProvider.provideActivityById(it.activityId)
                                            },
                                        ) { activities ->
                                            MoodWithActivities(
                                                moodTrack = moodTrack,
                                                moodActivityList = activities.toList().filterNotNull(),
                                            )
                                        }
                                    }
                                }
                        },
                    ) {
                        it.toList()
                    }
                }
            }.flowOn(dispatchers.io)
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
}