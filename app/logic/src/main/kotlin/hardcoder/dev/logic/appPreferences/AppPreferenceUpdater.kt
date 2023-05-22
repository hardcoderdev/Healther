package hardcoder.dev.logic.appPreferences

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppPreferenceUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(appPreference: AppPreference) = withContext(dispatcher) {
        appDatabase.appPreferenceQueries.upsert(
            id = PREFERENCES_ID,
            firstLaunchTime = appPreference.firstLaunchTime,
            lastAppReviewRequestTime = null
        )
    }

    companion object {
        private const val PREFERENCES_ID = 0
    }
}