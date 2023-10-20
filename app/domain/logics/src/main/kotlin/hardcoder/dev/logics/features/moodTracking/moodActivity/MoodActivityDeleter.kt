package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodActivityDao
import kotlinx.coroutines.withContext

class MoodActivityDeleter(
    private val moodActivityDao: MoodActivityDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        moodActivityDao.deleteById(id)
    }
}