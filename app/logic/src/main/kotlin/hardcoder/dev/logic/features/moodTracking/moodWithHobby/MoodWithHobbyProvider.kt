package hardcoder.dev.logic.features.moodTracking.moodWithHobby

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.entities.features.moodTracking.MoodWithHobbies
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MoodWithHobbyProvider(
    private val appDatabase: AppDatabase,
    private val moodTrackProvider: MoodTrackProvider,
    private val hobbyProvider: HobbyProvider
) {

    fun provideMoodWithHobbyList(dayRange: ClosedRange<Instant>): Flow<List<MoodWithHobbies>> {
        return moodTrackProvider.provideAllMoodTracksByDayRange(dayRange = dayRange)
            .flatMapLatest { tracks ->
                if (tracks.isEmpty()) flowOf(emptyList())
                else combine(
                    tracks.map { moodTrack ->
                        appDatabase.moodWithHobbyQueries.provideAllMoodWithHobbiesTrackByMoodTrackId(
                            moodTrack.id
                        )
                            .asFlow()
                            .map { it.executeAsList() }
                            .flatMapLatest { moodWithHobbyTracks ->
                                if (moodWithHobbyTracks.isEmpty()) {
                                    flowOf(
                                        MoodWithHobbies(
                                            moodTrack = moodTrack,
                                            hobbyList = emptyList()
                                        )
                                    )
                                } else {
                                    combine(
                                        moodWithHobbyTracks.map {
                                            hobbyProvider.provideHobbyById(it.hobbyId)
                                        }
                                    ) { hobbyTracks ->
                                        MoodWithHobbies(
                                            moodTrack = moodTrack,
                                            hobbyList = hobbyTracks.toList().filterNotNull()
                                        )
                                    }
                                }
                            }
                    }
                ) {
                    it.toList()
                }
            }
    }

    fun provideHobbyListByMoodTrackId(id: Int) = appDatabase.moodWithHobbyQueries
        .provideAllMoodWithHobbiesTrackByMoodTrackId(id)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { moodWithHobbyTracks ->
            if (moodWithHobbyTracks.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    moodWithHobbyTracks.map {
                        hobbyProvider.provideHobbyById(it.hobbyId)
                    }
                ) { hobbyTracks ->
                    hobbyTracks.toList().filterNotNull()
                }
            }
        }
}