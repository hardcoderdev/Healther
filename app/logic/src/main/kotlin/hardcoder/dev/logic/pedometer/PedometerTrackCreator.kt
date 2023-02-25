package hardcoder.dev.logic.pedometer

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PedometerTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createPedometerTrack(
        date: Long,
        stepsCount: Int,
        caloriesCount: Float,
        wastedTimeInMinutes: Int
    ) = withContext(dispatcher) {
        appDatabase.pedometerTrackQueries.insert(
            id = null,
            stepsCount = stepsCount,
            caloriesCount = caloriesCount,
            wastedTimeInMinutes = wastedTimeInMinutes,
            date = date
        )
    }
}