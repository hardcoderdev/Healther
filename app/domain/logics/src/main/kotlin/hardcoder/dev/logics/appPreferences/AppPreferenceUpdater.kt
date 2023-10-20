package hardcoder.dev.logics.appPreferences

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.appPreferences.AppPreferencesDao
import hardcoder.dev.database.entities.appPreferences.AppPreferences
import hardcoder.dev.entities.appPreferences.AppPreference
import kotlinx.coroutines.withContext

class AppPreferenceUpdater(
    private val appPreferencesDao: AppPreferencesDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(appPreference: AppPreference) = withContext(dispatchers.io) {
        appPreferencesDao.upsert(
            AppPreferences(
                id = PREFERENCES_ID,
                firstLaunchTime = appPreference.firstLaunchTime,
            )
        )
    }

    companion object {
        private const val PREFERENCES_ID = 0
    }
}