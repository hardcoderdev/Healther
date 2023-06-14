package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodWithActivityCreator(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun create(
        id: Int? = null,
        moodTrackId: Int,
        activityId: Int
    ) = withContext(ioDispatcher) {
        appDatabase.moodWithActivityQueries.upsert(
            id = id,
            activityId = activityId,
            moodTrackId = moodTrackId
        )
    }
}