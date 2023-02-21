package hardcoder.dev.logic.updaters

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.WaterTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(waterTrack: WaterTrack) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.update(
            waterTrack.date,
            waterTrack.millilitersCount,
            waterTrack.drinkType,
            waterTrack.id
        )
    }
}