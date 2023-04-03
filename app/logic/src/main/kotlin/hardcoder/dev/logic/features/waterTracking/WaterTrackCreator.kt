package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.entities.features.waterTracking.DrinkType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

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
            date = date.toInstant(TimeZone.currentSystemDefault()),
            millilitersCount = millilitersCount,
            drinkTypeId = drinkType.id
        )
    }
}