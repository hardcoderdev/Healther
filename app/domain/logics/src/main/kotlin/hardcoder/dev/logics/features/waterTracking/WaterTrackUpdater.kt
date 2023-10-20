package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
import hardcoder.dev.validators.features.waterTracking.CorrectMillilitersCount
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackUpdater(
    private val waterTrackDao: WaterTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        creationInstant: Instant,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int,
    ) = withContext(dispatchers.io) {
        waterTrackDao.update(
            WaterTrack(
                id = id,
                creationInstant = creationInstant,
                millilitersCount = millilitersCount.data,
                drinkTypeId = drinkTypeId,
            )
        )
    }
}