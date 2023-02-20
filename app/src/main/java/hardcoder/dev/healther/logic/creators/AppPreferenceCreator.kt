package hardcoder.dev.healther.logic.creators

import hardcoder.dev.healther.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppPreferenceCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createAppPreference() = withContext(dispatcher) {
        appDatabase.appPreferenceQueries.insert(
            id = null,
            isFirstLaunch = false
        )
    }
}