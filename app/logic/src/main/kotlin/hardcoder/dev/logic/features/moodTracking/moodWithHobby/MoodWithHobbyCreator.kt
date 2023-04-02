package hardcoder.dev.logic.features.moodTracking.moodWithHobby

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodWithHobbyCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        id: Int? = null,
        moodTrackId: Int,
        hobbyId: Int
    ) = withContext(dispatcher) {
        appDatabase.moodWithHobbyTrackQueries.upsert(
            id = id ?: idGenerator.nextId(),
            hobbyId = hobbyId,
            moodTrackId = moodTrackId
        )
    }
}