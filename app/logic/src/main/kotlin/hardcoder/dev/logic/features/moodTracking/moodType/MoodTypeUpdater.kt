package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.entities.features.moodTracking.MoodType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(moodType: MoodType) = withContext(dispatcher) {
        appDatabase.moodTypeQueries.update(
            name = moodType.name,
            iconId = moodType.icon.id,
            positivePercentage = moodType.positivePercentage,
            id = moodType.id
        )
    }
}