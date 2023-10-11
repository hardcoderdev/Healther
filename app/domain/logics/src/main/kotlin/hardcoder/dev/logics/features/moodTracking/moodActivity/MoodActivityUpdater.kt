package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import kotlinx.coroutines.withContext

class MoodActivityUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectActivityName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.moodActivityQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
        )
    }
}