package hardcoder.dev.logic.updaters

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.AppPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppPreferenceUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(appPreference: AppPreference) = withContext(dispatcher) {
        appDatabase.appPreferenceQueries.update(
            isFirstLaunch = appPreference.isFirstLaunch,
            id = appPreference.id
        )
    }
}