package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ActivityDeleter(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        appDatabase.activityQueries.deleteById(id)
    }
}