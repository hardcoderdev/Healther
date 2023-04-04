package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ActivityUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(activity: Activity) = withContext(dispatcher) {
        appDatabase.activityQueries.update(
            id = activity.id,
            name = activity.name,
            iconId = activity.icon.id
        )
    }
}