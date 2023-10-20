package hardcoder.dev.logics.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.dao.features.moodTracking.MoodTrackDao
import kotlinx.coroutines.withContext

class MoodTrackDeleter(
    private val moodTrackDao: MoodTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        moodTrackDao.deleteById(id)
    }

    suspend fun deleteAllTracksByMoodTypeId(id: Int) = withContext(dispatchers.io) {
        moodTrackDao.deleteAllTracksByMoodTypeId(id)
    }
}