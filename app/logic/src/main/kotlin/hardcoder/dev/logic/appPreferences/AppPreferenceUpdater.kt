package hardcoder.dev.logic.appPreferences

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppPreferenceUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun update(appPreference: AppPreference) = withContext(ioDispatcher) {
        appDatabase.appPreferenceQueries.upsert(
            id = PREFERENCES_ID,
            firstLaunchTime = appPreference.firstLaunchTime,
            lastAppReviewRequestTime = appPreference.lastAppReviewRequestTime
        )
    }

    companion object {
        private const val PREFERENCES_ID = 0
    }
}