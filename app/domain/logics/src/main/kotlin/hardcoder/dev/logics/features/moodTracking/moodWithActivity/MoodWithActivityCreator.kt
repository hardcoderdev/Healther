package hardcoder.dev.logics.features.moodTracking.moodWithActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodWithActivityDao
import hardcoder.dev.database.entities.features.moodTracking.MoodActivityCrossRef
import kotlinx.coroutines.withContext

class MoodWithActivityCreator(
    private val moodWithActivityDao: MoodWithActivityDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        moodTrackId: Int,
        activityId: Int,
    ) = withContext(dispatchers.io) {
        moodWithActivityDao.upsert(
            MoodActivityCrossRef(
                moodActivityId = activityId,
                moodTrackId = moodTrackId,
            ),
        )
    }
}