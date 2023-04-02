package hardcoder.dev.logic.features.moodTracking.hobby

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HobbyTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(hobbyTrack: HobbyTrack) = withContext(dispatcher) {
        appDatabase.hobbyTrackQueries.update(
            id = hobbyTrack.id,
            name = hobbyTrack.name,
            iconResourceName = hobbyTrack.iconResourceName
        )
    }
}