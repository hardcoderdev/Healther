package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        appDatabase.moodTrackQueries.deleteById(id)
    }

    suspend fun deleteAllTracksByMoodTypeId(id: Int) = withContext(dispatcher) {
        appDatabase.moodTrackQueries.deleteAllTracksByMoodTypeId(id)
    }
}