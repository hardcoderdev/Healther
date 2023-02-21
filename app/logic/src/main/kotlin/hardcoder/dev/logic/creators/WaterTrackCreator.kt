package hardcoder.dev.logic.creators

import hardcoder.dev.entities.DrinkType
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WaterTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createWaterTrack(
        date: Long,
        millilitersCount: Int,
        drinkType: DrinkType
    ) = withContext(dispatcher) {
        appDatabase.waterTrackQueries.insert(
            id = null,
            date = date,
            millilitersCount = millilitersCount,
            drinkType = drinkType
        )
    }
}