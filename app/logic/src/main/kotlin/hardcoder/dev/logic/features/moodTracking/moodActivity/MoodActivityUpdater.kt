package hardcoder.dev.logic.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class MoodActivityUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectActivityName,
        icon: hardcoder.dev.icons.Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.moodActivityQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
        )
    }
}