package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodWithActivityCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        id: Int? = null,
        moodTrackId: Int,
        activityId: Int
    ) = withContext(dispatcher) {
        appDatabase.moodWithActivityQueries.upsert(
            id = id,
            activityId = activityId,
            moodTrackId = moodTrackId
        )
    }
}