package hardcoder.dev.logic.features.moodTracking.moodWithHobby

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodWithHobbyTrack
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodWithHobbyTrack as MoodWithHobbyTrackEntity

class MoodWithHobbyProvider(private val appDatabase: AppDatabase) {

    fun provideMoodWithHobbyTracks(moodTrackId: Int) = appDatabase.moodWithHobbyTrackQueries
        .provideAllMoodWithHobbiesTrackByMoodTrackId(moodTrackId)
        .asFlow()
        .map {
            it.executeAsList().map { moodWithHobbyTrackDatabase ->
                moodWithHobbyTrackDatabase.toEntity()
            }
        }

    private fun MoodWithHobbyTrack.toEntity() = MoodWithHobbyTrackEntity(
        id = id, moodTrackId = moodTrackId, hobbyTrackId = hobbyId
    )
}