package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import kotlinx.coroutines.withContext

class MoodTypeDeleter(
    private val appDatabase: AppDatabase,
    private val moodTrackDeleter: MoodTrackDeleter,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        moodTrackDeleter.deleteAllTracksByMoodTypeId(id)
        appDatabase.moodTypeQueries.deleteById(id)
    }
}