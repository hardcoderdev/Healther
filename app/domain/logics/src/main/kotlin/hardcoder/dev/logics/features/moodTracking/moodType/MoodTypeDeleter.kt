package hardcoder.dev.logics.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodTypeDao
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackDeleter
import kotlinx.coroutines.withContext

class MoodTypeDeleter(
    private val moodTrackDeleter: MoodTrackDeleter,
    private val moodTypeDao: MoodTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        moodTrackDeleter.deleteAllTracksByMoodTypeId(id)
        moodTypeDao.deleteById(id)
    }
}