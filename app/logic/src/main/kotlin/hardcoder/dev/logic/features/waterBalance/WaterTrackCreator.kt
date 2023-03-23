package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.waterTracking.DrinkType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createWaterTrack(
        date: Long,
        millilitersCount: Int,
        drinkType: DrinkType
    ) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.insert(
            id = idGenerator.nextId(),
            date = date,
            millilitersCount = millilitersCount,
            drinkTypeId = drinkType.id
        )
    }
}