package hardcoder.dev.logic.features.moodTracking.hobby

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HobbyTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        appDatabase.hobbyTrackQueries.deleteById(id)
    }
}