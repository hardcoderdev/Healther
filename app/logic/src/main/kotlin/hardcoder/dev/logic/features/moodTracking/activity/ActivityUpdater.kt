package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.withContext

class ActivityUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun update(
        id: Int,
        name: CorrectActivityName,
        icon: LocalIcon
    ) = withContext(dispatchers.io) {
        appDatabase.moodActivityQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id
        )
    }
}