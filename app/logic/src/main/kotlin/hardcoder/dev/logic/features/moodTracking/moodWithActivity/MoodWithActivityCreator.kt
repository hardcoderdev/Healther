package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class MoodWithActivityCreator(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun create(
        id: Int? = null,
        moodTrackId: Int,
        activityId: Int
    ) = withContext(dispatchers.io) {
        appDatabase.moodWithActivityQueries.upsert(
            id = id,
            activityId = activityId,
            moodTrackId = moodTrackId
        )
    }
}