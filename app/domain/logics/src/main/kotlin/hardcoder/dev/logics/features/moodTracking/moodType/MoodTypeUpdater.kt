package hardcoder.dev.logics.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodTypeDao
import hardcoder.dev.database.entities.features.moodTracking.MoodType
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.moodTracking.CorrectMoodTypeName
import kotlinx.coroutines.withContext

class MoodTypeUpdater(
    private val moodTypeDao: MoodTypeDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectMoodTypeName,
        icon: Icon,
        positivePercentage: Int,
    ) = withContext(dispatchers.io) {
        moodTypeDao.update(
            MoodType(
                name = name.data,
                iconId = icon.id,
                positivePercentage = positivePercentage,
                id = id,
            ),
        )
    }
}