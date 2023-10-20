package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
import hardcoder.dev.validators.features.waterTracking.CorrectMillilitersCount
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackCreator(
    private val waterTrackDao: WaterTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        dateTime: Instant,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int,
    ) = withContext(dispatchers.io) {
        waterTrackDao.insert(
            waterTrack = WaterTrack(
                creationInstant = dateTime,
                millilitersCount = millilitersCount.data,
                drinkTypeId = drinkTypeId,
            ),
        )
    }
}