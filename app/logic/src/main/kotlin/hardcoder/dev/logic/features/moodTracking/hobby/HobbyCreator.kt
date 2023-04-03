package hardcoder.dev.logic.features.moodTracking.hobby

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HobbyCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: String,
        iconId: Long,
    ) = withContext(dispatcher) {
        appDatabase.hobbyQueries.insert(
            id = idGenerator.nextId(),
            name = name,
            iconId = iconId
        )
    }
}