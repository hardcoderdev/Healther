package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.entities.features.moodTracking.Hobby
import hardcoder.dev.logic.entities.features.moodTracking.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyCreator
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyDeleter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val moodWithHobbyDeleter: MoodWithHobbyDeleter,
    private val moodWithHobbyCreator: MoodWithHobbyCreator
) {

    suspend fun update(
        moodTrack: MoodTrack,
        selectedHobbies: List<Hobby>
    ) = withContext(dispatcher) {
        appDatabase.moodTrackQueries.update(
            id = moodTrack.id,
            moodTypeId = moodTrack.moodType.id,
            date = moodTrack.date
        )

        moodWithHobbyDeleter.deleteAllHobbiesByMoodTrackId(moodTrack.id)

        selectedHobbies.forEach { hobbyTrack ->
            moodWithHobbyCreator.create(
                moodTrackId = moodTrack.id,
                hobbyId = hobbyTrack.id
            )
        }
    }
}