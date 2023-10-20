package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import kotlinx.coroutines.withContext

class WaterTrackDeleter(
    private val waterTrackDao: WaterTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        waterTrackDao.deleteById(id)
    }

    suspend fun deleteAllTracksByDrinkTypeId(id: Int) = withContext(dispatchers.io) {
        waterTrackDao.deleteAllTracksByDrinkTypeId(id)
    }
}