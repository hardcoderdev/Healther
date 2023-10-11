package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.validators.features.waterTracking.CorrectMillilitersCount
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        date: Instant,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.waterTrackQueries.update(
            id = id,
            date = date,
            millilitersCount = millilitersCount.data,
            drinkTypeId = drinkTypeId,
        )
    }
}