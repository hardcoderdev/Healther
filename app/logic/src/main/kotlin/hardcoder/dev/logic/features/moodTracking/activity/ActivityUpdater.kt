package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ActivityUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun update(
        id: Int,
        name: CorrectActivityName,
        icon: LocalIcon
    ) = withContext(ioDispatcher) {
        appDatabase.activityQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id
        )
    }
}