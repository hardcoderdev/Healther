package hardcoder.dev.logic.features.moodTracking.activity

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ActivityCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: CorrectActivityName,
        icon: LocalIcon,
    ) = withContext(ioDispatcher) {
        appDatabase.activityQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id
        )
    }
}