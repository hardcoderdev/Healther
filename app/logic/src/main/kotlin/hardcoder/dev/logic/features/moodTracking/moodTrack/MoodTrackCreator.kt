package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.extensions.toMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class MoodTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        moodType: MoodType,
        date: LocalDateTime
    ) = withContext(dispatcher) {
        val moodTrackId = idGenerator.nextId()
        appDatabase.moodTrackQueries.insert(
            id = moodTrackId,
            moodTypeId = moodType.id,
            creationTime = date.toMillis()
        )
        return@withContext moodTrackId
    }
}