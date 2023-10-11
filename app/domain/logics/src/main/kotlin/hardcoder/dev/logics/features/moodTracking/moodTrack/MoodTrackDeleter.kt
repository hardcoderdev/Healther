package hardcoder.dev.logics.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class MoodTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        appDatabase.moodTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByMoodTypeId(id: Int) = withContext(dispatchers.io) {
        appDatabase.moodTrackQueries.deleteAllTracksByMoodTypeId(id)
    }
}