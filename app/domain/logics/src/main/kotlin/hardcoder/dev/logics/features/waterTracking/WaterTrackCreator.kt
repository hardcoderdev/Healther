package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.validators.features.waterTracking.CorrectMillilitersCount
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        dateTime: Instant,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.waterTrackQueries.insert(
            id = idGenerator.nextId(),
            date = dateTime,
            millilitersCount = millilitersCount.data,
            drinkTypeId = drinkTypeId,
        )
    }
}