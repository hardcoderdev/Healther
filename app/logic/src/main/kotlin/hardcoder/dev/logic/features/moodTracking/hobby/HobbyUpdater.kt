package hardcoder.dev.logic.features.moodTracking.hobby

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.entities.features.moodTracking.Hobby
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HobbyUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(hobby: Hobby) = withContext(dispatcher) {
        appDatabase.hobbyQueries.update(
            id = hobby.id,
            name = hobby.name,
            iconId = hobby.icon.id
        )
    }
}