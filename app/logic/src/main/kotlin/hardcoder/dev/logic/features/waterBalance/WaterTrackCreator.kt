package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.extensions.toMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class WaterTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createWaterTrack(
        date: LocalDateTime,
        millilitersCount: Int,
        drinkType: DrinkType
    ) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.insert(
            id = idGenerator.nextId(),
            date = date.toMillis(),
            millilitersCount = millilitersCount,
            drinkTypeId = drinkType.id
        )
    }
}