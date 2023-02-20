package hardcoder.dev.healther.logic.updaters

import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.entities.AppPreference
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