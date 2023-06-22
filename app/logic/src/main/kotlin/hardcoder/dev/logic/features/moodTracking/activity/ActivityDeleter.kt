package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class ActivityDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        appDatabase.moodActivityQueries.deleteById(id)
    }
}