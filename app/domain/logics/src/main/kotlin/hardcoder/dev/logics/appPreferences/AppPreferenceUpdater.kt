package hardcoder.dev.logic.appPreferences

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class AppPreferenceUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(appPreference: hardcoder.dev.entities.appPreferences.AppPreference) = withContext(dispatchers.io) {
        appDatabase.appPreferenceQueries.upsert(
            id = PREFERENCES_ID,
            firstLaunchTime = appPreference.firstLaunchTime,
        )
    }

    companion object {
        private const val PREFERENCES_ID = 0
    }
}