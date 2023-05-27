package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class WaterTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(
        id: Int,
        date: LocalDateTime,
        millilitersCount: CorrectMillilitersCount,
        drinkType: DrinkType
    ) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.update(
            id = id,
            date = date.toInstant(TimeZone.currentSystemDefault()),
            millilitersCount = millilitersCount.data,
            drinkTypeId = drinkType.id,
        )
    }
}