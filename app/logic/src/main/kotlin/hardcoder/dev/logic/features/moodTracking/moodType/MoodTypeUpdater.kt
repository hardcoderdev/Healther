package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.withContext

class MoodTypeUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun update(
        id: Int,
        name: CorrectMoodTypeName,
        icon: LocalIcon,
        positivePercentage: Int
    ) = withContext(dispatchers.io) {
        appDatabase.moodTypeQueries.update(
            name = name.data,
            iconId = icon.id,
            positivePercentage = positivePercentage,
            id = id
        )
    }
}