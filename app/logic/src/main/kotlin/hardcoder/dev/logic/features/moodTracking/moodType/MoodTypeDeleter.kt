package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeDeleter(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val moodTrackDeleter: MoodTrackDeleter
) {

    suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        moodTrackDeleter.deleteAllTracksByMoodTypeId(id)
        appDatabase.moodTypeQueries.deleteById(id)
    }
}