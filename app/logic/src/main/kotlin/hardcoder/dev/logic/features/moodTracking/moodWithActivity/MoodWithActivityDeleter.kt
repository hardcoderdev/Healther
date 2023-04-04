package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodWithActivityDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteAllActivitiesByMoodTrackId(moodTrackId: Int) = withContext(dispatcher) {
        appDatabase.moodWithActivityQueries.deleteAllActivitiesByMoodTrackId(moodTrackId)
    }
}