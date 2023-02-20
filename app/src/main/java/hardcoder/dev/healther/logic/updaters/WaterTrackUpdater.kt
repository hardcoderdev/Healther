package hardcoder.dev.healther.logic.updaters

import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.entities.WaterTrack
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