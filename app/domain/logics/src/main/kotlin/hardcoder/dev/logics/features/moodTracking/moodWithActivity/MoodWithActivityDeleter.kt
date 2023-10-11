package hardcoder.dev.logics.features.moodTracking.moodWithActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class MoodWithActivityDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteAllActivitiesByMoodTrackId(moodTrackId: Int) = withContext(dispatchers.io) {
        appDatabase.moodWithActivityQueries.deleteAllActivitiesByMoodTrackId(moodTrackId)
    }
}