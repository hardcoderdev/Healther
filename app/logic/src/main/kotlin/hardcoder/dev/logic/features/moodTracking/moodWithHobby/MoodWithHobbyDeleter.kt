package hardcoder.dev.logic.features.moodTracking.moodWithHobby

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodWithHobbyDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteAllHobbiesByMoodTrackId(moodTrackId: Int) = withContext(dispatcher) {
        appDatabase.moodWithHobbyTrackQueries.deleteAllHobbiesByMoodTrackId(moodTrackId)
    }
}