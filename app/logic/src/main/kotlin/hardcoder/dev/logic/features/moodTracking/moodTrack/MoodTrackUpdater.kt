package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(moodTrack: MoodTrack) = withContext(dispatcher) {
        appDatabase.moodTrackQueries.update(
            id = moodTrack.id,
            moodTypeId = moodTrack.moodType.id,
            creationTime = moodTrack.date.toEpochMilliseconds()
        )
    }
}