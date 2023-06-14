package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class WaterTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun createWaterTrack(
        date: LocalDateTime,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int
    ) = withContext(ioDispatcher) {
        appDatabase.waterTrackQueries.insert(
            id = idGenerator.nextId(),
            date = date.toInstant(TimeZone.currentSystemDefault()),
            millilitersCount = millilitersCount.data,
            drinkTypeId = drinkTypeId
        )
    }
}