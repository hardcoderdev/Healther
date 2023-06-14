package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun update(
        id: Int,
        date: LocalDateTime,
        millilitersCount: CorrectMillilitersCount,
        drinkTypeId: Int
    ) = withContext(ioDispatcher) {
        appDatabase.waterTrackQueries.update(
            id = id,
            date = date.toInstant(TimeZone.currentSystemDefault()),
            millilitersCount = millilitersCount.data,
            drinkTypeId = drinkTypeId
        )
    }
}