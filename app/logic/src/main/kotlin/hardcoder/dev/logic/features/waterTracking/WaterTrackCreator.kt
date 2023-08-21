package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.waterTracking.validators.CorrectMillilitersCount
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun createWaterTrack(
        dateTime: Instant,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int,
    ): Int {
        val waterTrackId = idGenerator.nextId()
        withContext(dispatchers.io) {
            appDatabase.waterTrackQueries.insert(
                id = waterTrackId,
                date = dateTime,
                millilitersCount = millilitersCount.data,
                drinkTypeId = drinkTypeId,
            )
        }

        return waterTrackId
    }
}