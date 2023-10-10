package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import kotlinx.coroutines.withContext

class MoodActivityCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectActivityName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.moodActivityQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
        )
    }
}